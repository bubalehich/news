package ru.clevertec.news.mapper;

import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.model.comment.CommentViewModel;

public interface CommentMapper {
    public CommentViewModel mapToViewModel(Comment news);
}
