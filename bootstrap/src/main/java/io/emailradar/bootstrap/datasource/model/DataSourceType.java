package io.emailradar.bootstrap.datasource.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum DataSourceType {
    FILE("file"),
    HDFS("hdfs"),
    S3("s3"),
    HTTP("http"),
    HTTPS("https");

    private final String protocol;
    private static final Map<String, DataSourceType> protocolMap = new HashMap<>();

    static {
        for (DataSourceType dataSourceType : values()) {
            protocolMap.put(dataSourceType.protocol, dataSourceType);
        }
    }

    public static DataSourceType valueOfProtocol(String protocol) {
        return protocolMap.get(protocol);
    }
}
