package hexlet.code;

import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
            var request = "url=https://test-domain.org/example/path";
            client.post(NamedRoutes.urlsPath(), request);
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://test-domain.org");
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
            var request = "url=https://your-domain.org:8080/example/path";
            var response = client.post(NamedRoutes.urlsPath(), request);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://your-domain.org:8080");

            var requestFail = "url=badUrlHere";
            var responseFail = client.post(NamedRoutes.urlsPath(), requestFail);
            assertThat(responseFail.code()).isEqualTo(200);
            assertFalse(responseFail.body().string().contains("badUrlHere"));
        });
    }

    @Test
    public void testCreateBadUrl() {
        JavalinTest.test(app, (server, client) -> {
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
            MockResponse mockResponse = new MockResponse().setResponseCode(200)
                    .setBody(readFixture("testPage.html"));
            mockServer.enqueue(mockResponse);
            mockServer.enqueue(mockResponse);
            mockServer.start();

            //Устанавливаем базовый урл серверу
            HttpUrl baseUrl = mockServer.url("/");
            log.info("MockUrl: {}", baseUrl);

            //Кидаем тестовый кейс в бд (базовый урл будет тестовым)
            var request = "url=" + baseUrl;
            var response = client.post(NamedRoutes.urlsPath(), request);
            assertThat(response.code()).isEqualTo(200);

            //Делаем check для переданного урла
            Long testedId = 1L;
            var request2 = NamedRoutes.checksPath(testedId);
            var responseCheck = client.post(request2);
            assertThat(responseCheck.code()).isEqualTo(200);

            var testedUrl = CheckRepository.findLastCheck(testedId);

            assertEquals(200, testedUrl.get().getStatusCode());
            assertEquals("https://ya.title", testedUrl.get().getTitle());
            assertEquals("Yandex-H1", testedUrl.get().getH1());
            assertEquals("Yandex-description", testedUrl.get().getDescription());
            assertFalse(testedUrl.get().getFormattedDate().isBlank());
        });
    }

    @AfterAll
    public static void shutdownMockWebServer() throws IOException {
        mockServer.shutdown();
    }

    public static Path getFixturePath(String filename) {
        return Paths.get("src", "test", "resources", "fixtures", filename)
                .toAbsolutePath().normalize();
    }

    public static String readFixture(String filename) throws IOException {
        Path filePath = getFixturePath(filename);
        return Files.readString(filePath).trim();
    }
}
