package io.spamradar.bootstrap.service.api;

import io.spamradar.bootstrap.exception.DataSourceException;
import io.spamradar.bootstrap.model.DataSourcePostRequest;

import java.net.URISyntaxException;

public interface DataSourceService {

    void createDataSource(DataSourcePostRequest dataSourcePostRequest) throws DataSourceException, URISyntaxException;
}
