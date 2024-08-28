package hexlet.code;

import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

@Slf4j
public class MainTest {
    Javalin app;
    static MockWebServer mockServer;

    @BeforeAll
    public static void startMockWebServer() {
        mockServer = new MockWebServer();
    }


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
    public void testIndexPage() {
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
                    .orElseThrow(NotFoundResponse::new)
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

    @Test
    public void testCheckUrl() {
        JavalinTest.test(app, (server, client) -> {
            //Генерим тут страницу в Мок для передачи приложению
            StringBuilder htmlContent = new StringBuilder()
                    .append("<html>")
                    .append("<head>")
                    .append("<title>https://ya.title</title>")
                    .append("<meta name=\"description\" content=\"Yandex-description\">")
                    .append("</head>")
                    .append("<body>")
                    .append("<h1>Yandex-H1</h1>")
                    .append("</body>")
                    .append("</html>");
            MockResponse mockResponse = new MockResponse().setResponseCode(200)
                    .setBody(htmlContent.toString());
            mockServer.enqueue(mockResponse);
            mockServer.start();

            //Устанавливаем базовый урл серверу
            HttpUrl baseUrl = mockServer.url("/");
            log.info("baseUrl=== " + baseUrl);

            //Кидаем тестовый кейс в бд (базовый урл будет тестовым)
            var request = "url=" + baseUrl;
            var response = client.post(NamedRoutes.urlsPath(), request);
            assertThat(response.code()).isEqualTo(200);

            //Делаем check для переданного урла
            var request2 = NamedRoutes.checksPath("1");
            var responseCheck = client.post(request2);
            var responseCheckBody = responseCheck.body().string();

            assertThat(responseCheck.code()).isEqualTo(200);
            assertThat(responseCheckBody).contains("<td>200</td>");
            assertThat(responseCheckBody).contains("<td>" + baseUrl
                    .toString()
                    .replaceAll("/+$", "") + "</td>");

            //Тут надо будет парсить html метаданные и передавать их для теста
            Document document = Jsoup.parse(responseCheckBody);
//            Document documentDirect = Jsoup.connect(baseUrl.toString()).get();

            assertEquals("https://ya.title", document.title());
            assertEquals("Yandex-H1", document.selectFirst("h1").text());
            assertEquals("Yandex-description", document.selectFirst("meta[name=description]")
                    .attr("content"));
        });
    }

    @AfterAll
    public static void shutdownMockWebServer() throws IOException {
        mockServer.shutdown();
    }
}
