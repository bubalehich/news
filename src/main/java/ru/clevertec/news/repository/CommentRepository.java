package ru.clevertec.news.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.news.entity.Comment;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, UUID>, CrudRepository<Comment, UUID> {
    List<Comment> findAllWithPagination(Pageable pageable);
}
