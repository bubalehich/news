package ru.clevertec.news.util.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum CommentSort {
    ID_ASC(Sort.by(Sort.Direction.ASC, "id")),
    ID_DESC(Sort.by(Sort.Direction.DESC, "id")),
    TEXT_ASC(Sort.by(Sort.Direction.DESC, "text")),
    DATE_ASC(Sort.by(Sort.Direction.ASC, "time"));

    private final Sort sortValue;
}
