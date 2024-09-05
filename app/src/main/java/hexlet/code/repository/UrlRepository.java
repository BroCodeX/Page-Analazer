package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setString(1, url.getName());
            LocalDateTime createdAt = url.getCreatedAt();
            preparedStmt.setTimestamp(2, Timestamp.valueOf(createdAt));

            preparedStmt.executeUpdate();
            var generatedKeys = preparedStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB has not returned an id for the insert");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        String sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql)) {
            preparedStmt.setLong(1, id);
            ResultSet set = preparedStmt.executeQuery();
            if (set.next()) {
                String name = set.getString("name");
                LocalDateTime createdAt = set.getTimestamp("created_at").toLocalDateTime();
                Url url = new Url(name, createdAt);
                url.setId(id);
                return Optional.of(url);
            } else {
                return Optional.empty();
            }
        }
    }

    public static Optional<Url> find(String url) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var preparedStmt = conn.prepareStatement(sql)) {
            preparedStmt.setString(1, url);
            ResultSet set = preparedStmt.executeQuery();
            if (set.next()) {
                String name = set.getString("name");
                LocalDateTime createdAt = set.getTimestamp("created_at").toLocalDateTime();
                Long id = set.getLong("id");
                Url urlModel = new Url(name, createdAt);
                urlModel.setId(id);
                return Optional.of(urlModel);
            } else {
                return Optional.empty();
            }
        }
    }

    public static List<Url> getEntries() throws SQLException {
        String sql = "SELECT * FROM urls";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql)) {
            ResultSet set = preparedStmt.executeQuery();
            List<Url> result = new ArrayList<>();
            while (set.next()) {
                Long id = set.getLong("id");
                String name = set.getString("name");
                LocalDateTime createdAt = set.getTimestamp("created_at").toLocalDateTime();
                Url url = new Url(name, createdAt);
                url.setId(id);
                result.add(url);
                result.sort(Comparator.comparingLong(Url::getId));
            }
            return result;
        }
    }
}
