package ru.clevertec.news.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.repository.NewsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NewsService {
    private NewsRepository newsRepository;

    private CommentService commentService;

    @Transactional
    public News create(String text) {
        var news = new News();
        news.setArchive(false);
        news.setTime(LocalDateTime.now());
        news.setText(text);

        return newsRepository.save(news);
    }


    @Transactional
    public News update(UUID id, String text) {
        var news = newsRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("News with id: %s not found.", id.toString())));

        news.setText(text);

        return newsRepository.save(news);
    }

    @Transactional(readOnly = true)
    public List<News> getAll(Pageable pageable) {
        return newsRepository.findAllWithPagination(pageable);
    }

    @Transactional(readOnly = true)
    public News getById(UUID id) {
        return newsRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("News with id: %s not found.", id.toString())));
    }

    @Transactional
    public News unarchiveNews(UUID id) {
        var news = newsRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("News with id: %s not found.", id.toString())));

        news.setArchive(false);
        return newsRepository.save(news);
    }

    @Transactional
    public News archiveNews(UUID id) {
        var news = newsRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("News with id: %s not found.", id.toString())));

        news.setArchive(true);
        return newsRepository.save(news);
    }
}
