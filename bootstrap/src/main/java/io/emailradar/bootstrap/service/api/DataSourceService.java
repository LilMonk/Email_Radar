package io.emailradar.bootstrap.service.api;

import io.emailradar.bootstrap.exception.DataSourceException;
import io.emailradar.bootstrap.model.DataSourcePostRequest;

import java.net.URISyntaxException;

public interface DataSourceService {

    void createDataSource(DataSourcePostRequest dataSourcePostRequest) throws DataSourceException, URISyntaxException;
}
