package ru.clevertec.news.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.clevertec.news.exception.ValidationException;
import ru.clevertec.news.model.news.NewsMutationModel;

@Component
public class NewsValidator {
    public void validate(NewsMutationModel model) {
        if (StringUtils.isEmpty(model.getText())) {
            throw new ValidationException("Text in news can't be blank.");
        }
    }
}
