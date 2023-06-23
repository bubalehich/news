package ru.clevertec.news.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.util.sort.NewsSort;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NewsService {
    private NewsRepository newsRepository;

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
                -> new EntityNotFoundException(String.format("News with id: %s not found.", id.toString())));

        news.setText(text);
        news.setTitle(title);

        return newsRepository.save(news);
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

    @Transactional(readOnly = true)
    public Page<News> getAllWithPagination(Integer offset, Integer limit, NewsSort sort) {
        return newsRepository.findAll(PageRequest.of(offset, limit, sort.getSortValue()));
    }
}
