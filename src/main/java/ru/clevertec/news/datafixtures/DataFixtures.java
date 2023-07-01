package ru.clevertec.news.datafixtures;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ro.polak.springboot.datafixtures.DataFixture;
import ro.polak.springboot.datafixtures.DataFixtureSet;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.repository.NewsRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataFixtures implements DataFixture {
    private static final int COUNT_OF_NEWS = 10;
    private static final int COUNT_OF_COMMENTS = 100;

    @Value("${data-fixtures.enabled}")
    private boolean enableDataFixtures;

    private final Faker faker = new Faker();

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;

    @Override
    public void load() {
        var news = generateNews();
        generateComments(news);
    }

    @Override
    public boolean canBeLoaded() {
        return enableDataFixtures;
    }

    @Override
    public DataFixtureSet getSet() {
        return DataFixtureSet.DICTIONARY;
    }

    private List<News> generateNews() {
        List<News> news = new ArrayList<>();
        for (int i = 0; i <= COUNT_OF_NEWS; i++) {
            var newsItem = new News(
                    UUID.randomUUID(),
                    convertToLocalDateTimeViaInstant(faker.date().birthday()),
                    faker.beer().name(),
                    faker.backToTheFuture().quote(),
                    new ArrayList<>(),
                    true
            );
            news.add(newsItem);
        }
        newsRepository.saveAll(news);

        return news;
    }

    private void generateComments(List<News> news) {
        List<Comment> comments = new ArrayList<>();
        for (News newsItem : news) {
            for (int i = 0; i < COUNT_OF_COMMENTS; i++) {
                var comment = new Comment(
                        UUID.randomUUID(),
                        convertToLocalDateTimeViaInstant(faker.date().birthday()),
                        faker.chuckNorris().fact(),
                        faker.artist().name(),
                        newsItem
                );
                comments.add(comment);
            }
        }
        commentRepository.saveAll(comments);
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
