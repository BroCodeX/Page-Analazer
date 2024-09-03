package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UrlModel {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private List<UrlCheck> urlChecks;

    public UrlModel(String name, LocalDateTime createdAt) {
        this.name = name;
        this.createdAt = createdAt;
        urlChecks = new ArrayList<>();
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return createdAt.format(formatter);
    }

    public void addCheck(UrlCheck check) {
        //check.setUrlId(this.id);
        urlChecks.add(check);
    }

    public void addChecks(List<UrlCheck> checks) {
        urlChecks.addAll(checks);
    }

    public void deleteCheck(UrlCheck check) {
        check.setUrlId(null);
        urlChecks.remove(check);
    }
}
