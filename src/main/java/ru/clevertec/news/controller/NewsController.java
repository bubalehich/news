package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.model.comment.CommentSearchCriteria;
import ru.clevertec.news.model.news.NewsMutationModel;
import ru.clevertec.news.model.news.NewsSearchCriteria;
import ru.clevertec.news.model.news.NewsViewModel;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.validator.NewsValidator;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class NewsController implements NewsApi {
    private final NewsService newsService;

    private final NewsValidator newsValidator;

    private final NewsMapper newsMapper;

    @Override
    public ResponseEntity<NewsViewModel> createNews(NewsMutationModel model) {
        newsValidator.validate(model);

        var news = newsService.create(model.getText());
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
    public ResponseEntity<Page<NewsViewModel>> getNews(NewsSearchCriteria criteria, Pageable pageRequest) {
        return null;
    }

    @Override
    public ResponseEntity<NewsViewModel> updateNews(UUID id, NewsMutationModel model) {
        var updatedNews = newsService.update(id, model.getText());
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
    public ResponseEntity<NewsViewModel> archive(UUID id) {
        var news = newsService.archiveNews(id);
        var newsViewModel = newsMapper.mapToViewModel(news);

        return ResponseEntity.ok(newsViewModel);
    }

    @Override
    public ResponseEntity<Page<NewsViewModel>> getComments(CommentSearchCriteria criteria, Pageable pageable) {
        if (!CollectionUtils.isEmpty(criteria.getFilters())) {
            var news = newsService.getAll(pageable);
            List<NewsViewModel> viewNews = news.stream().map(newsMapper::mapToViewModel).toList();

            return ResponseEntity.ok(new PageImpl<>(viewNews, pageable, viewNews.size()));
        }
        //TODO ???????? wtf
        return null;
    }
}
