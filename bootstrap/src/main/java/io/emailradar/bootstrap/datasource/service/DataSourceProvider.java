package io.emailradar.bootstrap.datasource.service;

import io.emailradar.bootstrap.constant.Label;
import io.emailradar.bootstrap.datasource.model.DataSource;
import io.emailradar.bootstrap.datasource.model.DataSourceType;
import io.emailradar.bootstrap.exception.DataSourceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * This is a data source provider class that will provide {@link DataSource} instance.<br/>
 * Use this class instead of creating your own {@link DataSource} instance because
 * this will validate the source and resolve the label.
 */
@Slf4j
@Component
public class DataSourceProvider {
    /**
     * Provides a data source instance. Validates the source url and
     * resolves the label.
     * @param sourceUrl try to give a valid url.
     * @param sourceLabel this will be resolved into {@link Label} enum value.
     * @return a {@link DataSource} instance.
     * @throws URISyntaxException if wrong url string is provided while creating {@link URI} instance.
     * @throws DataSourceException if invalid source url is provided.
     */
    public DataSource getDataSourceInstance(String sourceUrl, String sourceLabel) throws URISyntaxException, DataSourceException {
        if (!isValidDataSource(sourceUrl))
            throw new DataSourceException("Invalid source url.");

        URI uri = new URI(sourceUrl);
        String protocol = uri.getScheme();
        Label label = Label.getLabelOrDefault(sourceLabel);
        return new DataSource(uri, DataSourceType.valueOfProtocol(protocol), label);
    }

    private boolean isValidDataSource(String sourceUrl) {

        List<String> dataSourceTypesList = Arrays.stream(DataSourceType.values())
                .map(DataSourceType::getProtocol)
                .toList();
        String[] dataSourceTypes = dataSourceTypesList.toArray(new String[0]);

        log.debug("Registering schemes in UrlValidator : {}", dataSourceTypesList);

        UrlValidator urlValidator = new UrlValidator(dataSourceTypes);
        return urlValidator.isValid(sourceUrl);
    }
}
