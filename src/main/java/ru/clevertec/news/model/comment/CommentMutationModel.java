package ru.clevertec.news.model.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CommentMutationModel {
    @NotBlank
    private String text;
    @NotBlank
    private String username;
    @NotNull
    private UUID newsId;
}
