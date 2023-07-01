package ru.clevertec.news.service;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.util.sort.NewsSort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NewsService {
    private static final String NAME_TITLE = "title";
    private static final String NAME_TEXT = "text";
    private static final float TEXT_BOOSTING = 1.1f;
    private static final float TITLE_BOOSTING = 2.0f;

    private NewsRepository newsRepository;

    private EntityManager entityManager;

    @Transactional
    public News create(String text, String title) {
        var news = new News();
        news.setArchive(false);
        news.setTime(LocalDateTime.now());
        news.setText(text);
        news.setTitle(title);

        return newsRepository.save(news);
    }

    @Transactional
    public News update(UUID id, String text, String title) {
        var news = newsRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("News with id: %s not found.", id)));

        news.setText(text);
        news.setTitle(title);

        return newsRepository.save(news);
    }

    @Transactional(readOnly = true)
    public News getById(UUID id) {
        return newsRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("News with id: %s not found.", id)));
    }

    @Transactional
    public News unarchiveNews(UUID id) {
        var news = newsRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("News with id: %s not found.", id)));

        news.setArchive(false);
        return newsRepository.save(news);
    }

    @Transactional
    public News archiveNews(UUID id) {
        var news = newsRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("News with id: %s not found.", id)));

        news.setArchive(true);
        return newsRepository.save(news);
    }

    @Transactional(readOnly = true)
    public Page<News> getAllWithPagination(Integer offset, Integer limit, NewsSort sort) {
        return newsRepository.findAll(PageRequest.of(offset, limit, sort.getSortValue()));
    }

    @Transactional(readOnly = true)
    public List<News> search(String searchValue, Integer offset, Integer limit) {
        SearchSession searchSession = Search.session(entityManager);

        SearchResult<News> searchResult = searchSession.search(News.class)
                .where(news -> news.bool(b -> {
                    b.must(news.matchAll());
                    b.must(news.match()
                            .field(NAME_TITLE).boost(TITLE_BOOSTING)
                            .field(NAME_TEXT).boost(TEXT_BOOSTING)
                            .matching(searchValue));
                }))
                .sort(SearchSortFactory::score)
                .fetch(offset, limit);

        return searchResult.hits();
    }
}
