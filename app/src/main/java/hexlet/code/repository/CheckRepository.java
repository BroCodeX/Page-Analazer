package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

public class CheckRepository extends BaseRepository {

    public static void save(UrlCheck check) throws SQLException {
        String sql = "INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setLong(1, check.getUrlId());
            preparedStmt.setInt(2, check.getStatusCode());
            preparedStmt.setString(3, check.getH1());
            preparedStmt.setString(4, check.getTitle());
            preparedStmt.setString(5, check.getDescription());
            preparedStmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            preparedStmt.executeUpdate();
            var generatedKeys = preparedStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB has not returned an id for the insert");
            }
        }
    }

    public static Optional<UrlCheck> findLastCheck(Long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY id DESC";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql)) {
            preparedStmt.setLong(1, urlId);
            ResultSet set = preparedStmt.executeQuery();
            if (set.next()) {
                Long id = set.getLong("id");
                String title = set.getNString("title");
                String h1 = set.getNString("h1");
                String description = set.getNString("description");
                LocalDateTime createdAtCheck = set.getTimestamp("created_at").toLocalDateTime();
                int statusCode = set.getInt("status_code");
                UrlCheck check = new UrlCheck(title, h1, description, statusCode);
                check.setCreatedAt(createdAtCheck);
                check.setId(id);
                return Optional.of(check);
            } else {
                return Optional.empty();
            }
        }
    }

    public static List<UrlCheck> getEntries() throws SQLException {
        String sql = "SELECT * FROM url_checks ORDER BY id";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql)) {
            ResultSet set = preparedStmt.executeQuery();
            List<UrlCheck> result = new ArrayList<>();
            while (set.next()) {
                Long id = set.getLong("id");
                String title = set.getNString("title");
                String h1 = set.getNString("h1");
                String description = set.getNString("description");
                LocalDateTime createdAtCheck = set.getTimestamp("created_at").toLocalDateTime();
                int statusCode = set.getInt("status_code");
                UrlCheck check = new UrlCheck(title, h1, description, statusCode);
                check.setCreatedAt(createdAtCheck);
                check.setId(id);
                result.add(check);
            }
            return result;
        }
    }

    public static List<UrlCheck> findEntries(Long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY id";
        try (var conn = dataSource.getConnection();
             var preparedStmt = conn.prepareStatement(sql)) {
            preparedStmt.setLong(1, urlId);
            ResultSet set = preparedStmt.executeQuery();
            List<UrlCheck> result = new ArrayList<>();
            while (set.next()) {
                Long id = set.getLong("id");
                String title = set.getNString("title");
                String h1 = set.getNString("h1");
                String description = set.getNString("description");
                LocalDateTime createdAtCheck = set.getTimestamp("created_at").toLocalDateTime();
                int statusCode = set.getInt("status_code");
                UrlCheck check = new UrlCheck(title, h1, description, statusCode);
                check.setCreatedAt(createdAtCheck);
                check.setId(id);
                result.add(check);
            }
            return result;
        }
    }
}
