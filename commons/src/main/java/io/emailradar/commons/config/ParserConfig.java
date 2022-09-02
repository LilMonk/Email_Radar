package io.emailradar.commons.config;

import io.emailradar.commons.email.EmailContentHandler;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.MimeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserConfig {
    /**
     * Gives an instance of {@link MimeConfig}. For now, it is returning a lenient version of {@link MimeConfig} i.e.
     * {@link  MimeConfig#PERMISSIVE}
     *
     * @return {@link  MimeConfig#PERMISSIVE}
     */
    @Bean
    public MimeConfig getMimeConfig() {
        return MimeConfig.PERMISSIVE;
    }

    /**
     * Gives an instance of {@link MimeStreamParser} with {@link EmailContentHandler}
     * configured as the content handler.
     *
     * @param contentHandler custom content handler.
     * @return instance of MimeStreamParser.
     */
    @Bean
    public MimeStreamParser getMimeStreamParser(ContentHandler contentHandler, MimeConfig mimeConfig) {
        MimeStreamParser mimeStreamParser = new MimeStreamParser(mimeConfig);
        mimeStreamParser.setContentHandler(contentHandler);
        return mimeStreamParser;
    }
}
