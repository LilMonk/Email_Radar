package io.spamradar.bootstrap.controller;

import io.spamradar.bootstrap.exception.DataSourceException;
import io.spamradar.bootstrap.service.api.DataSourceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@AllArgsConstructor
@RestController
@RequestMapping("dataUpload")
@Slf4j
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @PostMapping("dataSource/url")
    public String postDataSourceUrl(@RequestBody String url) throws DataSourceException, URISyntaxException {
        log.info("Data Source URL : {}", url);
        dataSourceService.createDataSource(url);
        return "Success!!!";
    }

}
