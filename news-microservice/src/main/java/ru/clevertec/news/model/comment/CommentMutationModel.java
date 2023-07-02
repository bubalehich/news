package ru.clevertec.news.model.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentMutationModel {
    @NotBlank(message = "Text must not be blank")
    @Size(min = 3, max = 400)
    private String text;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50)
    private String username;
}
