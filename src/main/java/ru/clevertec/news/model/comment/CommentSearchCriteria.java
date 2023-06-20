package ru.clevertec.news.model.comment;

import lombok.Data;

import java.util.List;

@Data
public class CommentSearchCriteria {
    private List<String> filters;
}
