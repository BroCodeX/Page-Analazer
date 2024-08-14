package hexlet.code.controller;

import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;

public class UrlsController {

    public static void index(Context context) {
        context.render("urls/index.jte");
    }

    public static void show(Context context) {
        context.render("urls/show.jte");
    }
}
