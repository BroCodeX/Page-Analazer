package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.UrlModel;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UrlHandler;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void index(Context context) throws SQLException {
        List<UrlModel> urlModels = UrlRepository.getEntries();
        UrlsPage page = new UrlsPage(urlModels);
        page.setFlash(context.consumeSessionAttribute("flash"));
        page.setFlashType(context.consumeSessionAttribute("flashType"));
        context.render("urls/index.jte", model("page", page));
    }

    public static void show(Context context) throws SQLException {
        Long id = context.pathParamAsClass("id", Long.class).get();
        UrlModel url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url with " + id + " is not found"));
        UrlPage page = new UrlPage(url);
        context.render("urls/show.jte", model("page", page));
    }

    public static void create(Context context) throws SQLException {
        var name = context.formParamAsClass("url", String.class)
                .get();
        if (UrlHandler.isChecked(name)) {
            UrlModel urlModel = new UrlModel(name, LocalDateTime.now());
            UrlRepository.save(urlModel);
            context.sessionAttribute("flash", "Страница успешно добавлена");
            context.sessionAttribute("flashType", "success");
            context.redirect(NamedRoutes.urlsPath());
        } else {
            context.sessionAttribute("flash", UrlHandler.getMessage());
            context.sessionAttribute("flashType", "danger");
            context.redirect(NamedRoutes.rootPath());
        }
    }
}
