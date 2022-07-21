package io.spamradar.bootstrap.datasource.service;

import io.spamradar.bootstrap.datasource.model.DataSource;
import io.spamradar.bootstrap.datasource.model.DataSourceType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

class DataSourceProviderTest {

    @Test
    void getDataSourceInstance() throws URISyntaxException {
        String url = "http://abc.com";
        DataSource dataSource = DataSourceProvider.getDataSourceInstance(url);
//        Assertions.assertThat(dataSource.getUrl().getRawPath()).isEqualTo(url);
        Assertions.assertThat(dataSource.getDataSourceType()).isEqualTo(DataSourceType.HTTP);
    }
}