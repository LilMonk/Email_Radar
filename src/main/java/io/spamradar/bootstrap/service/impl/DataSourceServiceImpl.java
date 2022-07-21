package io.spamradar.bootstrap.service.impl;

import io.spamradar.bootstrap.datasource.model.DataSource;
import io.spamradar.bootstrap.datasource.model.DataSourceType;
import io.spamradar.bootstrap.datasource.reader.DataSourceReader;
import io.spamradar.bootstrap.datasource.reader.DataSourceReaderFactory;
import io.spamradar.bootstrap.datasource.service.DataSourceProvider;
import io.spamradar.bootstrap.email.EmailParser;
import io.spamradar.bootstrap.email.PrimitiveToCivilisedEmailConverter;
import io.spamradar.bootstrap.email.model.CivilisedEmail;
import io.spamradar.bootstrap.email.model.PrimitiveEmail;
import io.spamradar.bootstrap.exception.DataSourceException;
import io.spamradar.bootstrap.kafka.EmailProducer;
import io.spamradar.bootstrap.service.api.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.james.mime4j.MimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
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
    DataSourceReaderFactory dataSourceReaderFactory;

    @Override
    public void createDataSource(String sourceUrl) throws DataSourceException, URISyntaxException {
        if (!isValidDataSource(sourceUrl))
            throw new DataSourceException("Invalid source url.");
        // TODO: Create a datasource obj and register it to process using threading.
        // TODO: Make email parsing parallel.
        DataSource dataSource = DataSourceProvider.getDataSourceInstance(sourceUrl);
        DataSourceReader dataSourceReader = dataSourceReaderFactory.getDataSourceReader(dataSource);
        dataSourceReader.forEach(ip -> {
            PrimitiveEmail primitiveEmail;
            try {
                primitiveEmail = emailParser.parse(ip);
            } catch (MimeException | IOException e) {
                throw new RuntimeException(e);
            }
            CivilisedEmail civilisedEmail = PrimitiveToCivilisedEmailConverter.convertToCivilisedEmail(primitiveEmail);
            emailProducer.sendMessage(civilisedEmail, topic);
        });
    }

    private boolean isValidDataSource(String sourceUrl) {
        List<String> dataSourceTypesList = Arrays.stream(DataSourceType.values())
                .map(DataSourceType::getProtocol)
                .toList();
        String[] dataSourceTypes = dataSourceTypesList.toArray(new String[0]);

        log.debug("Registering schemes: {}", dataSourceTypesList);

        UrlValidator urlValidator = new UrlValidator(dataSourceTypes);
        return urlValidator.isValid(sourceUrl);
    }
}
