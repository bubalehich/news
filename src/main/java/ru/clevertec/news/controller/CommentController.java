package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.model.comment.CommentMutationModel;
import ru.clevertec.news.model.comment.CommentSearchCriteria;
import ru.clevertec.news.model.comment.CommentViewModel;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.validator.CommentValidator;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class CommentController implements CommentApi {
    private final CommentService commentService;
    private final CommentValidator commentValidator;
    private final CommentMapper commentMapper;

    @Override
    public ResponseEntity<CommentViewModel> createComment(UUID newsId, CommentMutationModel model) {
        commentValidator.validate(model);

        var comment = commentService.create(model.getText(), model.getUsername(), newsId);
        var commentViewModel = commentMapper.mapToViewModel(comment);

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(commentViewModel.getId())
                .toUri();

        return ResponseEntity.created(uri).body(commentViewModel);
    }

    @Override
    public ResponseEntity<CommentViewModel> getComment(UUID newsId, UUID id) {
        var comment = commentService.getById(id);
        var commentViewModel = commentMapper.mapToViewModel(comment);

        return ResponseEntity.ok(commentViewModel);
    }

    @Override
    public ResponseEntity<Page<CommentViewModel>> getComments(UUID newsId, CommentSearchCriteria criteria, Pageable pageable) {
        if (!CollectionUtils.isEmpty(criteria.getFilters())) {
            var comments = commentService.getAll(pageable);
            List<CommentViewModel> viewComments = comments.stream().map(commentMapper::mapToViewModel).toList();

            return ResponseEntity.ok(new PageImpl<>(viewComments, pageable, viewComments.size()));
        }
        //TODO ???????? wtf
        return null;
    }

    @Override
    public ResponseEntity<CommentViewModel> updateComment(UUID newsId, UUID commentId, CommentMutationModel model) {
        var updatedComment = commentService.update(model.getText(), commentId);
        var commentViewModel = commentMapper.mapToViewModel(updatedComment);

        return ResponseEntity.ok(commentViewModel);
    }

    @Override
    public ResponseEntity<CommentViewModel> deleteComment(UUID newsId, UUID commentId) {
        commentService.delete(commentId);

        return ResponseEntity.noContent().build();
    }
}
