package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.UrlCheck;
import hexlet.code.model.UrlModel;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlsController {

    public static void index(Context context) throws SQLException {
        List<UrlModel> urlModels = UrlRepository.getEntries();
        if (!CheckRepository.getEntries().isEmpty()) {
            for (UrlModel url : urlModels) {
                Long id = url.getId();
                LinkedList<UrlCheck> checks = new LinkedList<>(CheckRepository.findEntries(id));
                url.addCheck(checks.peekLast());
            }
        }
        UrlsPage page = new UrlsPage(urlModels);
        page.setFlash(context.consumeSessionAttribute("flash"));
        page.setFlashType(context.consumeSessionAttribute("flashType"));
        context.render("urls/index.jte", model("page", page));
    }

    public static void show(Context context) throws SQLException {
        Long id = context.pathParamAsClass("id", Long.class).get();
        UrlModel url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse(String.format("Url with %s is not found", id)));
        if (!CheckRepository.findEntries(id).isEmpty()) {
            url.addChecks(CheckRepository.findEntries(id));
        }

        UrlPage page = new UrlPage(url);
        page.setFlash(context.consumeSessionAttribute("flash"));
        page.setFlashType(context.consumeSessionAttribute("flashType"));
        context.render("urls/show.jte", model("page", page));
    }

    public static void create(Context context) throws SQLException {
        try {
            var name = context.formParamAsClass("url", String.class)
                    .get();
            log.info("Переданный урл: {}", name);
            URI uri = new URI(name);
            URL url = uri.toURL();
            String normalizedUrl = getNormalizeUrl(url);
            log.info("Нормализованный урл: {}", normalizedUrl);
            if (UrlRepository.find(normalizedUrl).isPresent()) {
                context.sessionAttribute("flash", "Страница уже существует");
                context.sessionAttribute("flashType", "warning");
                context.redirect(NamedRoutes.rootPath());
            } else {
                UrlModel urlModel = new UrlModel(normalizedUrl, LocalDateTime.now());
                UrlRepository.save(urlModel);
                context.sessionAttribute("flash", "Страница успешно добавлена");
                context.sessionAttribute("flashType", "success");
                context.redirect(NamedRoutes.urlsPath());
            }
        } catch (MalformedURLException | IllegalArgumentException | URISyntaxException ex) {
            log.error("URL не прошел валидацию");
            context.sessionAttribute("flash", "Некорректный URL");
            context.sessionAttribute("flashType", "danger");
            context.redirect(NamedRoutes.rootPath());
        }
    }

    public static void check(Context context) throws SQLException {
        Long id = context.pathParamAsClass("id", Long.class).get();
        UrlModel url = UrlRepository.find(id).get();
        try {
            Map<String, String> content = getHtmlContent(url.getName());
            String title = content.get("title");
            String h1 = content.get("h1");
            String description = content.get("description");
            int statusCode = Unirest.get(url.getName()).asString().getStatus();
            LocalDateTime createdAtCheck = LocalDateTime.now();

            UrlCheck check = new UrlCheck(title, h1, description, createdAtCheck, statusCode);
            check.setUrlId(url.getId());
            CheckRepository.save(check);
            url.addCheck(check);

            context.sessionAttribute("flash", "Страница успешно проверена");
            context.sessionAttribute("flashType", "success");
            context.redirect(NamedRoutes.urlPath(id));
        } catch (UnirestException ex) {
            context.sessionAttribute("flash", ex.getMessage());
            context.sessionAttribute("flashType", "danger");
            context.redirect(NamedRoutes.urlPath(id));
        }
        Unirest.config().reset();
    }

    public static String getNormalizeUrl(URL url) {
        String baseUrl = String.format("%s://%s", url.getProtocol(), url.getHost())
                .toLowerCase()
                .trim();
        if (url.getPort() != -1) {
            return String.format("%s:%s", baseUrl, url.getPort());
        }
        return baseUrl;
    }

    public static Map<String, String> getHtmlContent(String urlAddress) {
        Map<String, String> map = new HashMap<>();
        try {
            Document document = Jsoup.connect(urlAddress).timeout(5000).get();
            map.put("title", document.title());
            Element h1Element = document
                    .selectFirst("h1");
            map.put("h1", h1Element == null ? "" : h1Element.text());
            Element descriptionEl = document
                    .selectFirst("meta[name=description]");
            map.put("description", descriptionEl == null ? "" : descriptionEl.attr("content"));
        } catch (IOException ex) {
            throw new UnirestException("Страница не найдена или недоступна для проверки");
        }
        return map;
    }
}
