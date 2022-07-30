package io.spamradar.bootstrap.service.api;

import io.spamradar.bootstrap.exception.DataSourceException;

import java.net.URISyntaxException;

public interface DataSourceService {

    void createDataSource(String sourceUrl) throws DataSourceException, URISyntaxException;
}
