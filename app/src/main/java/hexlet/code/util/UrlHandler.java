package hexlet.code.util;

import hexlet.code.repository.UrlRepository;
import lombok.Getter;

import java.sql.SQLException;


public class UrlHandler {
    @Getter
    private static String message;

    public static boolean isChecked(String url) throws SQLException {
        if (UrlRepository.find(url).isPresent()) {
            message = "Страница уже существует";
            return false;
        } else if (url.length() < 15) {
            message = "тут будет Некорректный URL (пока в работе)";
            return false;
        }
        return true;
    }
}
