package ru.clevertec.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.model.comment.CommentViewModel;

@Mapper(config = MappersConfig.class)
public interface CommentMapper {
    @Mapping(target = "newsId", source = "comment.news.id")
    @Mapping(target = "time", source = "comment.time", dateFormat = "dd-MM-yyyy HH:mm:ss")
    CommentViewModel commentToCommentViewModel(Comment comment);
}
