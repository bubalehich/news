package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.model.news.NewsMutationModel;
import ru.clevertec.news.model.news.NewsViewModel;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.util.sort.NewsSort;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class NewsController implements NewsApi {
    private final NewsService newsService;

    private final NewsMapper newsMapper;

    @Override
    public ResponseEntity<NewsViewModel> createNews(NewsMutationModel model) {
        var news = newsService.create(model.getText(), model.getTitle());
        var newsViewModel = newsMapper.mapToViewModel(news);

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newsViewModel.getId())
                .toUri();

        return ResponseEntity.created(uri).body(newsViewModel);
    }

    @Override
    public ResponseEntity<NewsViewModel> getNews(UUID id) {
        var news = newsService.getById(id);
        var newsViewModel = newsMapper.mapToViewModel(news);

        return ResponseEntity.ok(newsViewModel);
    }

    @Override
    public ResponseEntity<NewsViewModel> updateNews(UUID id, NewsMutationModel model) {
        var updatedNews = newsService.update(id, model.getText(), model.getTitle());
        var newsViewModel = newsMapper.mapToViewModel(updatedNews);

        return ResponseEntity.ok(newsViewModel);
    }

    @Override
    public ResponseEntity<NewsViewModel> unarchive(UUID id) {
        var news = newsService.unarchiveNews(id);
        var newsViewModel = newsMapper.mapToViewModel(news);

        return ResponseEntity.ok(newsViewModel);
    }

    @Override
    public Page<NewsViewModel> searchNews(Integer offset, Integer limit, String searchValue) {
        var news = newsService.search(searchValue, offset, limit);
        var viewModels = news.stream().map(newsMapper::mapToViewModel).toList();

        return new PageImpl<>(viewModels, PageRequest.of(limit, offset), viewModels.size());
    }

    @Override
    public ResponseEntity<NewsViewModel> archive(UUID id) {
        var news = newsService.archiveNews(id);
        var newsViewModel = newsMapper.mapToViewModel(news);

        return ResponseEntity.ok(newsViewModel);
    }

    @Override
    public Page<NewsViewModel> getNews(Integer offset, Integer limit, NewsSort sort) {
        var news = newsService.getAllWithPagination(offset, limit, sort);
        var viewModels = news.map(newsMapper::mapToViewModel).toList();

        return new PageImpl<>(viewModels, PageRequest.of(limit, offset), viewModels.size());
    }
}
