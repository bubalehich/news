package ru.clevertec.news.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.news.entity.News;

import java.util.UUID;

@Repository
public interface NewsRepository extends CrudRepository<News, UUID> {
}
