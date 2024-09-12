package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setString(1, url.getName());
            preparedStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

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
                Url url = makeUrl(set, id);
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
                Long id = set.getLong("id");
                Url urlModel = makeUrl(set, id);
                return Optional.of(urlModel);
            } else {
                return Optional.empty();
            }
        }
    }

    public static List<Url> getEntries() throws SQLException {
        String sql = "SELECT * FROM urls ORDER BY id";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql)) {
            ResultSet set = preparedStmt.executeQuery();
            List<Url> result = new ArrayList<>();
            while (set.next()) {
                Long id = set.getLong("id");
                Url url = makeUrl(set, id);
                result.add(url);
            }
            return result;
        }
    }

    public static Url makeUrl(ResultSet set, Long id) throws SQLException {
        String name = set.getString("name");
        LocalDateTime createdAt = set.getTimestamp("created_at").toLocalDateTime();
        return new Url(id, name, createdAt);
    }
}
