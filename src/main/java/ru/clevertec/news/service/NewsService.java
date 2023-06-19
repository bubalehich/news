package ru.clevertec.news.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.repository.NewsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NewsService {
    private NewsRepository newsRepository;

    public News create(String text) {
        return null;
    }

    public News update(String text) {
        return null;
    }

    public boolean delete(UUID id) {
        return false;
    }

    public List<News> getAll() {
        return new ArrayList<>();
    }

    public News getById(UUID id) {
        return null;
    }

    public News unarchiveNews(UUID id) {
        return null;
    }

    public News archiveNews(UUID id) {
        return null;
    }
}
