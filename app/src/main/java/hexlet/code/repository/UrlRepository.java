package hexlet.code.repository;

import hexlet.code.model.UrlModel;

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

    public static void save(UrlModel urlModel) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setString(1, urlModel.getName());
            LocalDateTime createdAt = urlModel.getCreatedAt();
            preparedStmt.setTimestamp(2, Timestamp.valueOf(createdAt));

            preparedStmt.executeUpdate();
            var generatedKeys = preparedStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlModel.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB has not returned an id for the insert");
            }
        }
    }

    public static Optional<UrlModel> find(Long id) throws SQLException {
        String sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql)) {
            preparedStmt.setLong(1, id);
            ResultSet set = preparedStmt.executeQuery();
            if (set.next()) {
                String name = set.getString("name");
                LocalDateTime createdAt = set.getTimestamp("created_at").toLocalDateTime();
                UrlModel urlModel = new UrlModel(name, createdAt);
                urlModel.setId(id);
                return Optional.of(urlModel);
            } else {
                return Optional.empty();
            }
        }
    }

    public static Optional<UrlModel> find(String url) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var preparedStmt = conn.prepareStatement(sql)) {
            preparedStmt.setString(1, url);
            ResultSet set = preparedStmt.executeQuery();
            if (set.next()) {
                String name = set.getString("name");
                LocalDateTime createdAt = set.getTimestamp("created_at").toLocalDateTime();
                Long id = set.getLong("id");
                UrlModel urlModel = new UrlModel(name, createdAt);
                urlModel.setId(id);
                return Optional.of(urlModel);
            } else {
                return Optional.empty();
            }
        }
    }

    public static List<UrlModel> getEntries() throws SQLException {
        String sql = "SELECT * FROM urls";
        try (var conn = dataSource.getConnection();
                var preparedStmt = conn.prepareStatement(sql)) {
            ResultSet set = preparedStmt.executeQuery();
            List<UrlModel> result = new ArrayList<>();
            while (set.next()) {
                Long id = set.getLong("id");
                String name = set.getString("name");
                LocalDateTime createdAt = set.getTimestamp("created_at").toLocalDateTime();
                UrlModel urlModel = new UrlModel(name, createdAt);
                urlModel.setId(id);
                result.add(urlModel);
                result.sort(Comparator.comparingLong(UrlModel::getId));
            }
            return result;
        }
    }
}
