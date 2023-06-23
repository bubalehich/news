package ru.clevertec.news.model.news;

import lombok.Data;

import java.util.UUID;

@Data
public class NewsViewModel {
    private UUID id;

    private String text;

    private String title;

    private String time;

    private boolean isArchive;
}
