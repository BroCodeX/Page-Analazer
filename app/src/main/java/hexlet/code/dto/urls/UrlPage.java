package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.Check;
import hexlet.code.model.Url;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
public class UrlPage extends BasePage {
    private Url url;
    @Setter
    private List<Check> checkList;

    public UrlPage(Url url) {
        this.url = url;
    }
}
