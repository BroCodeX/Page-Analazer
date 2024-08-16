package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import io.javalin.http.Context;

import static io.javalin.rendering.template.TemplateUtil.model;

public class RootController {

    public static void index(Context context) {
        var page = new UrlPage();
        page.setFlash(context.consumeSessionAttribute("flash"));
        page.setFlashType(context.consumeSessionAttribute("flashType"));
        context.render("index.jte", model("page", page));
    }
}
