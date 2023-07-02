package ru.clevertec.news.search;

import jakarta.validation.constraints.NotNull;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class LuceneAnalysisConfig implements LuceneAnalysisConfigurer {
    private static final String CUSTOM = "Custom";

    @Override
    public void configure(@NotNull LuceneAnalysisConfigurationContext context) {
        context.analyzer(CUSTOM)
                .custom()
                .tokenizer(StandardTokenizerFactory.class)
                .charFilter(HTMLStripCharFilterFactory.class)
                .tokenFilter(LowerCaseFilterFactory.class)
                .tokenFilter(SnowballPorterFilterFactory.class)
                .param("language", "English")
                .tokenFilter(ASCIIFoldingFilterFactory.class);

        context.normalizer("lowercase")
                .custom()
                .tokenFilter(LowerCaseFilterFactory.class)
                .tokenFilter(ASCIIFoldingFilterFactory.class);
    }
}
