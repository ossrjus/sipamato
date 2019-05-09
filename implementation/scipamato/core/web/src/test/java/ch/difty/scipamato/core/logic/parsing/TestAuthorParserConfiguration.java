package ch.difty.scipamato.core.logic.parsing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestAuthorParserConfiguration {

    @Bean
    @Primary
    public AuthorParserFactory authorParserFactory() {
        return new DefaultAuthorParserFactory(AuthorParserStrategy.PUBMED);
    }
}
