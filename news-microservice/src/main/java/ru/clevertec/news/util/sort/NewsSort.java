package ru.clevertec.news.util.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum NewsSort {
    ID_ASC(Sort.by(Sort.Direction.ASC, "id")),
    ID_DESC(Sort.by(Sort.Direction.DESC, "id")),
    DATE_ASC(Sort.by(Sort.Direction.ASC, "time")),
    TEXT_ASC(Sort.by(Sort.Direction.ASC, "text")),
    TITLE_ASC(Sort.by(Sort.Direction.ASC, "title"));

    private final Sort sortValue;
}
