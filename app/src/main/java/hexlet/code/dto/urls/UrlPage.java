package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.UrlModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UrlPage extends BasePage {
    private UrlModel urlModel;
}
