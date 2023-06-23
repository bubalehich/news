package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.model.comment.CommentMutationModel;
import ru.clevertec.news.model.comment.CommentViewModel;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.util.sort.CommentSort;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class CommentController implements CommentApi {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Override
    public ResponseEntity<CommentViewModel> createComment(UUID newsId, CommentMutationModel model) {
        var comment = commentService.create(model.getText(), model.getUsername(), newsId);
        var commentViewModel = commentMapper.commentToCommentViewModel(comment);

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(commentViewModel.getId())
                .toUri();

        return ResponseEntity.created(uri).body(commentViewModel);
    }

    @Override
    public ResponseEntity<CommentViewModel> getComment(UUID newsId, UUID id) {
        var comment = commentService.getById(id);
        var commentViewModel = commentMapper.commentToCommentViewModel(comment);

        return ResponseEntity.ok(commentViewModel);
    }

    @Override
    public ResponseEntity<Page<CommentViewModel>> getComments(UUID newsId, Integer offset, Integer limit, CommentSort sort) {
        var comments = commentService.getAllWithPagination(newsId, offset, limit, sort);
        var viewModels = comments.map(commentMapper::commentToCommentViewModel);

        return ResponseEntity.ok(viewModels);
    }

    @Override
    public ResponseEntity<CommentViewModel> updateComment(UUID newsId, UUID commentId, CommentMutationModel model) {
        var updatedComment = commentService.update(model.getText(), commentId);
        var commentViewModel = commentMapper.commentToCommentViewModel(updatedComment);

        return ResponseEntity.ok(commentViewModel);
    }

    @Override
    public ResponseEntity<CommentViewModel> deleteComment(UUID newsId, UUID commentId) {
        commentService.delete(commentId);

        return ResponseEntity.noContent().build();
    }
}
