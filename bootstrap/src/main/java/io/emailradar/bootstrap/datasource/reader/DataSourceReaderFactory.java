package io.emailradar.bootstrap.datasource.reader;

import io.emailradar.bootstrap.datasource.model.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

/**
 * A factory class to provide {@link DataSourceReader} instance
 * according to the URL. <br>
 * E.g. It will give an instance of {@link FileDataSourceReader}
 * for file type datasource.
 */
@Component
public class DataSourceReaderFactory {
    private final boolean selfCloseInputStream;

    /**
     * Factory for providing {@link DataSourceReader} instance according to the URL.
     *
     * @param selfCloseInputStream true if you want application to close the input stream, false otherwise.
     *                             In case of false, remember to close the input stream.
     */
    public DataSourceReaderFactory(@Value("${bootstrap.datasource.reader.self.close.inputstream:true}") boolean selfCloseInputStream) {
        this.selfCloseInputStream = selfCloseInputStream;
    }

    /**
     * Get the {@link DataSourceReader} instance.
     *
     * @param dataSource data source information to read the data from.
     * @return {@link DataSourceReader} instance.
     */
    public DataSourceReader getDataSourceReader(DataSource dataSource) throws URISyntaxException {
        return switch (dataSource.getDataSourceType()) {
            case FILE -> new FileDataSourceReader(dataSource, selfCloseInputStream);
            case HDFS, S3, HTTP, HTTPS -> null; // TODO: Implement DataSourceReader for all these DataSources.
        };
    }
}
