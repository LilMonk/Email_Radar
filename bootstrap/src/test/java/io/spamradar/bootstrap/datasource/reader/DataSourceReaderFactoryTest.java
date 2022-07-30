package io.spamradar.bootstrap.datasource.reader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

@SpringBootTest
class DataSourceReaderFactoryTest {
    //    @Autowired
//    DataSourceReaderFactory dataSourceReaderFactory;
//    @Autowired
    @Value("${bootstrap.datasource.reader.self.close.inputstream:true}")
    private boolean selfCloseInputStream;

    @Test
    void getDataSourceReader() throws URISyntaxException {
//        DataSource dataSource = DataSourceProvider.getDataSourceInstance("file:///abc/");
//        DataSourceReader dataSourceReader = dataSourceReaderFactory.getDataSourceReader(dataSource);
//        Assertions.assertThat(dataSourceReader).isInstanceOf(FileDataSourceReader.class);
        System.out.println(selfCloseInputStream);
    }
}