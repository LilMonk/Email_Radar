package io.emailradar.bootstrap.service.impl;

import io.emailradar.bootstrap.datasource.model.DataSource;
import io.emailradar.bootstrap.datasource.reader.DataSourceReader;
import io.emailradar.bootstrap.datasource.reader.DataSourceReaderFactory;
import io.emailradar.bootstrap.datasource.service.DataSourceProvider;
import io.emailradar.bootstrap.exception.DataSourceException;
import io.emailradar.bootstrap.kafka.EmailProducer;
import io.emailradar.bootstrap.model.DataSourcePostRequest;
import io.emailradar.bootstrap.service.api.DataSourceService;
import io.emailradar.commons.email.EmailParser;
import io.emailradar.commons.email.mapper.PrimitiveToCivilisedEmailMapper;
import io.emailradar.commons.email.model.CivilisedEmail;
import io.emailradar.commons.email.model.EmailPayload;
import io.emailradar.commons.email.model.PrimitiveEmail;
import io.emailradar.commons.exception.EmailParseException;
import io.emailradar.commons.metadata.Metadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.mime4j.MimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

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
    PrimitiveToCivilisedEmailMapper primitiveToCivilisedEmailMapper;

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

            List<CivilisedEmail> civilisedEmail = primitiveToCivilisedEmailMapper.convertToCivilisedEmail(primitiveEmail);

            for (CivilisedEmail email : civilisedEmail) {
                EmailPayload emailPayload = EmailPayload.builder()
                        ._id()
                        .metadata(metadata)
                        .civilisedEmail(email)
                        .build();
                emailProducer.sendMessage(emailPayload, topic);
            }
        });
    }
}
