package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Check;
import hexlet.code.model.Url;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Tools;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlsController {

    public static void index(Context context) throws SQLException {
        List<Url> urls = UrlRepository.getEntries();
        Map<Long, Check> lastCheck = CheckRepository.getLastChecks();
        UrlsPage page = new UrlsPage(urls);
        page.setLastCheckMap(lastCheck);
        page.setFlash(context.consumeSessionAttribute("flash"));
        page.setFlashType(context.consumeSessionAttribute("flashType"));
        context.render("urls/index.jte", model("page", page));
    }

    public static void show(Context context) throws SQLException {
        Long id = context.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse(String.format("Url with %s is not found", id)));
        List<Check> checks = new ArrayList<>();
        if (!CheckRepository.findEntries(id).isEmpty()) {
            checks = CheckRepository.findEntries(id);
        }

        UrlPage page = new UrlPage(url);
        page.setCheckList(checks);
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
            String normalizedUrl = Tools.getNormalizeUrl(url);
            log.info("Нормализованный урл: {}", normalizedUrl);
            if (UrlRepository.find(normalizedUrl).isPresent()) {
                context.sessionAttribute("flash", "Страница уже существует");
                context.sessionAttribute("flashType", "warning");
                context.redirect(NamedRoutes.rootPath());
            } else {
                Url urlModel = new Url(normalizedUrl);
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
        Url url = UrlRepository.find(id).get();
        try {
            Check check = getHtmlContent(url.getName());
            check.setUrlId(url.getId());
            CheckRepository.save(check);

            context.sessionAttribute("flash", "Страница успешно проверена");
            context.sessionAttribute("flashType", "success");
            context.redirect(NamedRoutes.urlPath(id));
        } catch (UnirestException ex) {
            context.sessionAttribute("flash", ex.getMessage());
            context.sessionAttribute("flashType", "danger");
            context.redirect(NamedRoutes.urlPath(id));
        }
    }

    private static Check getHtmlContent(String urlAddress) {
        try {
            Connection.Response response = Jsoup.connect(urlAddress).timeout(5000).execute();
            int statusCode = response.statusCode();
            Document document = response.parse();
            String title = document.title();
            Element h1Element = document
                    .selectFirst("h1");
            String h1 = h1Element == null ? "" : h1Element.text();
            Element descriptionEl = document
                    .selectFirst("meta[name=description]");
            String description = descriptionEl == null ? "" : descriptionEl.attr("content");
            return new Check(statusCode, title, h1, description);
        } catch (IOException ex) {
            throw new UnirestException("Страница не найдена или недоступна для проверки");
        }
    }
}
