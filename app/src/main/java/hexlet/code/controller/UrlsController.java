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
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlsController {

    public static void index(Context context) throws SQLException {
        LinkedList<UrlModel> urlModels = UrlRepository.getEntries();
        if (!CheckRepository.getEntries().isEmpty()) {
            for (UrlModel url : urlModels) {
                Long id = url.getId();
                LinkedList<UrlCheck> checks = CheckRepository.findEntries(id);
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
            log.info(normalizedUrl);
            if (UrlRepository.find(normalizedUrl).isPresent()) {
                context.sessionAttribute("flash", "Страница уже существует");
                context.sessionAttribute("flashType", "danger");
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
            String title = "title";
            String h1 = "h1";
            String description = "description";
//        int statusCode = response.getStatus();
            Unirest.config().connectTimeout(5000);
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
            context.sessionAttribute("flash", "Timeout. Проверьте доступность домена");
            context.sessionAttribute("flashType", "danger");
            context.redirect(NamedRoutes.urlPath(id));
        }
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
}
