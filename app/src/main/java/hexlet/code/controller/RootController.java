package hexlet.code.controller;

import io.javalin.http.Context;

public class RootController {

    public static void index(Context context) {
        context.render("index.jte");
    }
}
