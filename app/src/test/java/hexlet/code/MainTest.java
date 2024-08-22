package hexlet.code;

import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

public class MainTest {

    Javalin app;

    @BeforeEach
    public final void getApp() throws SQLException, IOException {
        app = App.getApp();
    }

    @Test
    public void testRootPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("<form");
        });
    }

    @Test
    public void testUrlsIndexPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlShowPage() {
        JavalinTest.test(app, (server, client) -> {
            var request = "url=https://some-domain.org/example/path";
            client.post(NamedRoutes.urlsPath(), request);
            Long id = UrlRepository.find("https://some-domain.org")
                    .get()
                    .getId();
            var response = client.get(NamedRoutes.urlPath(id));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://some-domain.org");
        });
    }

    @Test
    public void testUrlShowPageNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath("777666"));
            assertThat(response.code()).isEqualTo(404);
            assertTrue(response.body().string().contains("Url with 777666 is not found"));
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var request = "url=https://some-domain.org/example/path";
            var response = client.post(NamedRoutes.urlsPath(), request);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://some-domain.org");

            var requestPort = "url=https://your-domain.org:8080/example/path";
            var responsePort = client.post(NamedRoutes.urlsPath(), requestPort);
            assertThat(responsePort.code()).isEqualTo(200);
            assertThat(responsePort.body().string()).contains("https://your-domain.org:8080");

            var requestFail = "url=badUrlHere";
            var responseFail = client.post(NamedRoutes.urlsPath(), requestFail);
            assertThat(responseFail.code()).isEqualTo(200);
            assertFalse(responseFail.body().string().contains("badUrlHere"));
        });
    }
}
