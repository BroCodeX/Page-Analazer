package hexlet.code;

import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class App {

    public static Javalin getApp() throws IOException {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.before(context -> context.contentType("text/html; charset=utf-8"));
        app.get(NamedRoutes.rootPath(), context -> context.result("Hello, World!"));
        return app;
    }

    public static void main(String[] args) throws IOException {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }
}
