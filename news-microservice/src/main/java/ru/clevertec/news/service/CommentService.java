package ru.clevertec.news.service;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.util.sort.CommentSort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_TEXT = "text";
    private static final float TEXT_BOOST_FACTOR = 1.1f;
    private static final float USERNAME_BOOST_FACTOR = 2.0f;

    private CommentRepository commentRepository;

    private NewsService newsService;

    private EntityManager entityManager;

    @Transactional
    @CachePut(value = "comment", key = "#result.id()")
    public Comment create(String text, String username, UUID newsId) {
        var news = newsService.getById(newsId);

        var comment = new Comment();
        comment.setText(text);
        comment.setTime(LocalDateTime.now());
        comment.setNews(news);
        comment.setUsername(username);

        return commentRepository.save(comment);
    }

    @CachePut(value = "comment", key = "#result.id()")
    @Transactional
    public Comment update(String text, UUID commentId) {
        var comment = commentRepository.findById(commentId).orElseThrow(()
                -> new EntityNotFoundException(String.format("Comment with id: %s not found.", commentId)));

        comment.setText(text);
        return commentRepository.save(comment);
    }

    @Transactional
    public boolean delete(UUID commentId) {
        var comment = commentRepository.findById(commentId).orElseThrow(()
                -> new EntityNotFoundException(String.format("Comment with id: %s not found.", commentId)));
        commentRepository.delete(comment);

        return true;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "comment")
    public Comment getById(UUID id) {
        return commentRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("Comment with id: %s not found.", id)));
    }

    @Transactional(readOnly = true)
    public Page<Comment> getAllWithPagination(UUID newsId, Integer offset, Integer limit, CommentSort sort) {
        return commentRepository.findAllByNewsId(newsId, PageRequest.of(offset, limit, sort.getSortValue()));
    }

    @Transactional(readOnly = true)
    public List<Comment> search(String searchValue, Integer offset, Integer limit) {
        SearchSession searchSession = Search.session(entityManager);

        SearchResult<Comment> searchResult = searchSession.search(Comment.class)
                .where(comment -> comment.bool(b -> {
                    b.must(comment.matchAll());
                    b.must(comment.match()
                            .field(FIELD_TEXT).boost(TEXT_BOOST_FACTOR)
                            .field(FIELD_USERNAME).boost(USERNAME_BOOST_FACTOR)
                            .matching(searchValue));
                }))
                .sort(SearchSortFactory::score)
                .fetch(offset, limit);

        return searchResult.hits();
    }
}
