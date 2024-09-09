package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.Check;
import hexlet.code.model.Url;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
public class UrlsPage extends BasePage {
    private List<Url> urls;
    @Setter
    private Map<Long, Check> lastCheckMap;

    public UrlsPage(List<Url> urls) {
        this.urls = urls;
    }
}
