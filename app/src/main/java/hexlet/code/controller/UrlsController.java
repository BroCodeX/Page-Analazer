package hexlet.code.controller;

import hexlet.code.dto.UrlsPage;
import hexlet.code.model.UrlModel;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void index(Context context) {
        context.render("urls/index.jte");
    }

    public static void show(Context context) {
        context.render("urls/show.jte");
    }

    public static void create(Context context) throws SQLException {
        try {
            var name = context.pathParamAsClass("name", String.class)
                    .check(value -> !value.isEmpty(), "Поле не должно быть пустым")
                    .get();
            UrlModel urlModel = new UrlModel(name, LocalDateTime.now());
            UrlRepository.save(urlModel);
            context.redirect(NamedRoutes.urlsPath());
        } catch (ValidationException ex) {
            var name = context.formParam("name");
            var page = new UrlsPage(UrlRepository.getEntries());
            context.render("urls/index.jte", model("page", page));
        }

    }
}
