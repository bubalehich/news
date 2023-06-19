package ru.clevertec.news.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;

    public Comment create(String text, String username) {
        return null;
    }

    public Comment update(String text, String username, UUID newsId) {
        return null;
    }

    public boolean delete(UUID newsId, UUID commentId) {
        //TODO throw an exep if not success
        return false;
    }

    public List<Comment> getAll() {
        return new ArrayList<>();
    }

    public Comment getById(UUID id) {
        return null;
    }
}
