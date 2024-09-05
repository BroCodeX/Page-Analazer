package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Url {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private List<Check> checks;

    public Url(String name) {
        this.name = name;
        checks = new ArrayList<>();
    }

    public Url(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        checks = new ArrayList<>();
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return createdAt.format(formatter);
    }

    public void addCheck(Check check) {
        //check.setUrlId(this.id);
        checks.add(check);
    }

    public void addChecks(List<Check> checksList) {
        this.checks.addAll(checksList);
    }

    public void deleteCheck(Check check) {
        check.setUrlId(null);
        checks.remove(check);
    }
}
