package ru.clevertec.news.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.news.entity.News;

import java.util.List;
import java.util.UUID;

@Repository
public interface NewsRepository extends PagingAndSortingRepository<News, UUID>, CrudRepository<News, UUID> {
    List<News> findAllWithPagination(Pageable pageable);
}
