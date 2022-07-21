package io.spamradar.bootstrap.config;

import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserConfig {

    /**
     * Gives an instance of {@link MimeStreamParser} with {@link io.spamradar.bootstrap.email.EmailContentHandler}
     * configured as the content handler.
     *
     * @param contentHandler custom content handler.
     * @return intance of MimeStreamParser.
     */
    @Bean
    public MimeStreamParser getMimeStreamParser(ContentHandler contentHandler) {
        MimeStreamParser mimeStreamParser = new MimeStreamParser();
        mimeStreamParser.setContentHandler(contentHandler);
        return mimeStreamParser;
    }
}
