package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.UrlModel;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UrlChecker;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;

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
        try {
            var name = context.formParamAsClass("url", String.class)
                    .check(value -> {
                        try {
                            return UrlChecker.getCheck(value);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }, UrlChecker.getMessage())
                    .get();
            UrlModel urlModel = new UrlModel(name, LocalDateTime.now());
            UrlRepository.save(urlModel);
            context.sessionAttribute("flash", "Страница успешно добавлена");
            context.sessionAttribute("flashType", "success");
            context.redirect(NamedRoutes.urlsPath());
        } catch (ValidationException ex) {
            var page = new UrlPage();
//            String errorMsg = "";
//            for (var validator : ex.getErrors().values()) {
//                for (var error : validator) {
//                    errorMsg = error.getMessage();
//                }
//            }
//            page.setFlash(errorMsg);
            page.setFlash(ex.getErrors().values().stream()
                    .flatMap(List::stream)
                    .map(ValidationError::getMessage)
                    .findFirst()
                    .orElse(""));
            page.setFlashType("danger");
            context.render("index.jte", model("page", page)).status(422);
        }


    }

//    public static void create(Context context) throws SQLException {
//        try {
//            var name = context.formParamAsClass("url", String.class)
//                    .check(value -> {
//                        try {
//                            return UrlChecker.getCheck(value);
//                        } catch (SQLException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }, "Некорректный URL")
//                    .get();
//            UrlModel urlModel = new UrlModel(name, LocalDateTime.now());
//            UrlRepository.save(urlModel);
//            context.sessionAttribute("flash", "Страница успешно добавлена");
//            context.sessionAttribute("flashType", "success");
//            context.redirect(NamedRoutes.urlsPath());
//        } catch (ValidationException ex) {
//            var page = new UrlPage();
//            String errorMsg = "sdf";
//            for (var validator : ex.getErrors().values()) {
//                for (var error : validator) {
//                    errorMsg = error.getMessage();
//                }
//            }
//            page.setFlash(errorMsg);
////            page.setFlash(ex.getErrors()
////                    .values()
////                    .stream()
////                    .map(error -> error.getFirst().getMessage())
////                    .toString());
//            page.setFlashType("danger");
//            context.render("index.jte", model("page", page)).status(422);
//        }
//    }
}
