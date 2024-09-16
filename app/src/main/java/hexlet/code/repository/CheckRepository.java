package hexlet.code.repository;

import hexlet.code.model.Check;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

public class CheckRepository extends BaseRepository {

    public static void save(Check check) throws SQLException {
        String sql = "INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setLong(1, check.getUrlId());
            preparedStmt.setInt(2, check.getStatusCode());
            preparedStmt.setString(3, check.getH1());
            preparedStmt.setString(4, check.getTitle());
            preparedStmt.setString(5, check.getDescription());
            preparedStmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            preparedStmt.executeUpdate();
            var generatedKeys = preparedStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB has not returned an id for the insert");
            }
        }
    }

    public static Optional<Check> findLastCheck(Long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY id DESC";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql)) {
            preparedStmt.setLong(1, urlId);
            ResultSet set = preparedStmt.executeQuery();
            if (set.next()) {
                Check check = makeCheck(set, urlId);
                return Optional.of(check);
            } else {
                return Optional.empty();
            }
        }
    }

    public static List<Check> findEntries(Long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY id";
        try (var conn = dataSource.getConnection();
             var preparedStmt = conn.prepareStatement(sql)) {
            preparedStmt.setLong(1, urlId);
            ResultSet set = preparedStmt.executeQuery();
            List<Check> result = new ArrayList<>();
            while (set.next()) {
                Check check = makeCheck(set, urlId);
                result.add(check);
            }
            return result;
        }
    }

    public static Map<Long, Check> getLastChecks() throws SQLException {
        String sql = "SELECT * FROM url_checks ORDER BY id ASC";
        try (var conn = dataSource.getConnection();
             var preparedStmt = conn.prepareStatement(sql)) {
            ResultSet set = preparedStmt.executeQuery();
            Map<Long, Check> result = new HashMap<>();
            while (set.next()) {
                Long urlId = set.getLong("url_id");
                Check check = makeCheck(set, urlId);
                result.put(urlId, check);
            }
            return result;
        }
    }

    public static Check makeCheck(ResultSet set, Long urlId) throws SQLException {
        Long id = set.getLong("id");
        String title = set.getString("title");
        String h1 = set.getString("h1");
        String description = set.getString("description");
        LocalDateTime createdAtCheck = set.getTimestamp("created_at").toLocalDateTime();
        int statusCode = set.getInt("status_code");
        return new Check(id, statusCode, title, h1, description, urlId, createdAtCheck);
    }
}
