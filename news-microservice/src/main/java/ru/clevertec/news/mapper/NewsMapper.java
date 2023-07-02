package ru.clevertec.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.model.news.NewsViewModel;

@Mapper(config = MappersConfig.class)
public interface NewsMapper {
    @Mapping(target = "time", source = "news.time", dateFormat = "dd-MM-yyyy HH:mm:ss")
    NewsViewModel mapToViewModel(News news);
}
