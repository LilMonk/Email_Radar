package io.spamradar.bootstrap.model;


import lombok.Data;

@Data
public class DataSourcePostRequest {
    private String url;
    private String label;
}
