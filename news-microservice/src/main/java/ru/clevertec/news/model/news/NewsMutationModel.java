package ru.clevertec.news.model.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsMutationModel {
    @NotBlank(message = "Text must not be blank")
    @Size(min = 3, max = 1000)
    private String text;

    @NotBlank(message = "Title must not be blank")
    @Size(min = 3, max = 50)
    private String title;
}
