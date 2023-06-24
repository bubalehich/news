package ru.clevertec.news.index;

import lombok.AllArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



@Transactional
@Component
@AllArgsConstructor
public class Indexer {

    private final EntityManager entityManager;

    private static final int THREAD_NUMBER = 4;

    public void indexPersistedData(String indexClassName) throws Exception {

        SearchSession searchSession = Search.session(entityManager);

        Class<?> classToIndex = Class.forName(indexClassName);
        MassIndexer indexer =
                searchSession
                        .massIndexer(classToIndex)
                        .threadsToLoadObjects(THREAD_NUMBER);

        indexer.startAndWait();
    }
}
