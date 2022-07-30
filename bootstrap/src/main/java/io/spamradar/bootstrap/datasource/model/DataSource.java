package io.spamradar.bootstrap.datasource.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.net.URI;

@Data
@AllArgsConstructor
@Builder
public class DataSource {
    URI url;
    DataSourceType dataSourceType;
}

/*
 * POJO : url, type
 *
 * */