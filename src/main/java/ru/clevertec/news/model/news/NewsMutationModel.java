package ru.clevertec.news.model.news;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewsMutationModel {
    @NotBlank
    private String text;
}
