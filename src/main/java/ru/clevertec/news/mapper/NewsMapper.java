package ru.clevertec.news.mapper;

import ru.clevertec.news.entity.News;
import ru.clevertec.news.model.news.NewsViewModel;

public interface NewsMapper {
    //TODO replace to mapstrict

    NewsViewModel mapToViewModel(News patient);

  //  NewsMutationRequest mapToMutationRequest(NewsMutationModel model);
}
