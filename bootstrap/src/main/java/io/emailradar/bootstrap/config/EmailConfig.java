package io.emailradar.bootstrap.config;

import io.emailradar.commons.email.EmailParser;
import io.emailradar.commons.email.EmailParserProvider;
import io.emailradar.commons.email.mapper.PrimitiveToCivilisedEmailMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {
    @Bean
    public EmailParser getEmailParser() {
        return EmailParserProvider.getInstance();
    }

    @Bean
    public PrimitiveToCivilisedEmailMapper getPrimitiveToCivilisedEmailMapper() {
        return new PrimitiveToCivilisedEmailMapper();
    }
}
