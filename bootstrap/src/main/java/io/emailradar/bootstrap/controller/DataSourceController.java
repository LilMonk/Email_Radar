package io.emailradar.bootstrap.controller;

import io.emailradar.bootstrap.exception.DataSourceException;
import io.emailradar.bootstrap.model.DataSourcePostRequest;
import io.emailradar.bootstrap.service.api.DataSourceService;
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
    public String postDataSourceUrl(@RequestBody DataSourcePostRequest dataSourcePostRequest) throws DataSourceException, URISyntaxException {
        log.info("Data Source Post Request : {}", dataSourcePostRequest);
        dataSourceService.createDataSource(dataSourcePostRequest);
        return "Success!!!";
    }

}
