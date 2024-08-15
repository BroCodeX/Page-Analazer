package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.UrlModel;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void index(Context context) throws SQLException {
        List<UrlModel> urlModels = UrlRepository.getEntries();
        UrlsPage page = new UrlsPage(urlModels);
        String flash = context.consumeSessionAttribute("flash");
        page.setFlash(flash);
        context.render("urls/index.jte", model("page", page));
    }

    public static void show(Context context) throws SQLException {
        Long id = context.pathParamAsClass("id", Long.class).get();
        UrlModel url = UrlRepository.find(id).orElseThrow(() -> new NotFoundResponse("Url has not found"));
        UrlPage page = new UrlPage(url);
        String flash = context.consumeSessionAttribute("flash");
        page.setFlash(flash);
        context.render("urls/show.jte", model("page", page));
    }

    public static void create(Context context) throws SQLException {
        try {
            var name = context.pathParamAsClass("name", String.class)
                    .check(value -> !value.isEmpty(), "Поле не должно быть пустым")
                    .get();
            UrlModel urlModel = new UrlModel(name, LocalDateTime.now());
            UrlRepository.save(urlModel);
            context.sessionAttribute("flash", "Урл был добавлен");
            context.redirect(NamedRoutes.urlsPath());
        } catch (ValidationException ex) {
            var page = new UrlPage();
            page.setFlash(ex.getMessage());
            context.render("urls/index.jte", model("page", page));
        }

    }
}
