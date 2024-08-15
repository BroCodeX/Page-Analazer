package hexlet.code.dto;

import hexlet.code.model.UrlModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UrlsPage extends BasePage {
    private List<UrlModel> urlModels;
}
