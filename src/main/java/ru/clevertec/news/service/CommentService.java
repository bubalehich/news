package ru.clevertec.news.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.util.sort.CommentSort;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;

    private NewsService newsService;

    @Transactional
    public Comment create(String text, String username, UUID newsId) {
        var news = newsService.getById(newsId);

        var comment = new Comment();
        comment.setText(text);
        comment.setTime(LocalDateTime.now());
        comment.setNews(news);
        comment.setUsername(username);

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment update(String text, UUID commentId) {
        var comment = commentRepository.findById(commentId).orElseThrow(()
                -> new EntityNotFoundException(String.format("Comment with id: %s not found.", commentId.toString())));

        comment.setText(text);
        return commentRepository.save(comment);
    }

    @Transactional
    public boolean delete(UUID commentId) {
        var comment = commentRepository.findById(commentId).orElseThrow(()
                -> new EntityNotFoundException(String.format("Comment with id: %s not found.", commentId.toString())));
        commentRepository.delete(comment);

        return true;
    }

    @Transactional(readOnly = true)
    public Comment getById(UUID id) {
        return commentRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("Comment with id: %s not found.", id.toString())));
    }

    @Transactional(readOnly = true)
    public Page<Comment> getAllWithPagination(UUID newsId, Integer offset, Integer limit, CommentSort sort) {
        return commentRepository.findAllByNewsId(newsId, PageRequest.of(offset, limit, sort.getSortValue()));
    }
}
