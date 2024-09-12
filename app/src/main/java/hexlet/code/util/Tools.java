package hexlet.code.util;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tools {

    public static String getFormattedDate(LocalDateTime createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return createdAt.format(formatter);
    }

    public static String getNormalizeUrl(URL url) {
        String baseUrl = String.format("%s://%s", url.getProtocol(), url.getHost())
                .toLowerCase()
                .trim();
        if (url.getPort() != -1) {
            return String.format("%s:%s", baseUrl, url.getPort());
        }
        return baseUrl;
    }
}
