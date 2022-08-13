package io.spamradar.bootstrap.service.impl;

import io.spamradar.bootstrap.datasource.metadata.Metadata;
import io.spamradar.bootstrap.datasource.model.DataSource;
import io.spamradar.bootstrap.datasource.reader.DataSourceReader;
import io.spamradar.bootstrap.datasource.reader.DataSourceReaderFactory;
import io.spamradar.bootstrap.datasource.service.DataSourceProvider;
import io.spamradar.bootstrap.email.EmailParser;
import io.spamradar.bootstrap.email.PrimitiveToCivilisedEmailConverter;
import io.spamradar.bootstrap.email.model.CivilisedEmail;
import io.spamradar.bootstrap.email.model.PrimitiveEmail;
import io.spamradar.bootstrap.exception.DataSourceException;
import io.spamradar.bootstrap.exception.EmailParseException;
import io.spamradar.bootstrap.kafka.EmailProducer;
import io.spamradar.bootstrap.model.DataSourcePostRequest;
import io.spamradar.bootstrap.service.api.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.mime4j.MimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Component
@Service
public class DataSourceServiceImpl implements DataSourceService {
    @Value("${kafka.topic.name}")
    String topic;

    @Autowired
    EmailParser emailParser;

    @Autowired
    EmailProducer emailProducer;

    @Autowired
    DataSourceProvider dataSourceProvider;

    @Autowired
    DataSourceReaderFactory dataSourceReaderFactory;

    @Autowired
    PrimitiveToCivilisedEmailConverter primitiveToCivilisedEmailConverter;

    // TODO: Change the name of this method. Refactor this method to make it simple.
    @Override
    public void createDataSource(DataSourcePostRequest dataSourcePostRequest) throws DataSourceException, URISyntaxException {
        String sourceUrl = dataSourcePostRequest.getUrl();
        String sourceLabel = dataSourcePostRequest.getLabel();

        // TODO: Create a datasource obj and register it to process using threading.
        // TODO: Make email parsing parallel.

        DataSource dataSource = dataSourceProvider.getDataSourceInstance(sourceUrl, sourceLabel);
        DataSourceReader dataSourceReader = dataSourceReaderFactory.getDataSourceReader(dataSource);

        dataSourceReader.forEach(ip -> {
            PrimitiveEmail primitiveEmail;
            try {
                primitiveEmail = emailParser.parse(ip);
            } catch (MimeException | IOException | EmailParseException e) {
                throw new RuntimeException(e);
            }

            // TODO: Create a metadataProvider to make this thing testable.
            Metadata metadata = Metadata.builder()
                    .sourceUrl(sourceUrl)
                    .label(sourceLabel)
                    .build();

            CivilisedEmail civilisedEmail = primitiveToCivilisedEmailConverter.convertToCivilisedEmail(primitiveEmail, metadata);
            emailProducer.sendMessage(civilisedEmail, topic);
        });
    }
}
