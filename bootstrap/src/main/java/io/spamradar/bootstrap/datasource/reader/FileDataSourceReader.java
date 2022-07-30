package io.spamradar.bootstrap.datasource.reader;

import io.spamradar.bootstrap.datasource.model.DataSource;

import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * {@link DataSourceReader} implementation to handle file based data source.
 */
public class FileDataSourceReader implements DataSourceReader {
    private final Iterator<InputStream> fileNamesIterator;

    /**
     * Handles file based data source.
     *
     * @param dataSource           to get the data source information.
     * @param selfCloseInputStream true if you want application to close the input stream, false otherwise.
     *                             In case of false, remember to close the input stream.
     */
    public FileDataSourceReader(DataSource dataSource, boolean selfCloseInputStream) {
        String dirPath = dataSource.getUrl().getPath();
        fileNamesIterator = new FileDataSourceInputStream(dirPath, selfCloseInputStream);
    }

    /**
     * Iterator to iterate over list of {@link java.io.InputStream}.
     *
     * @return custom {@link java.io.InputStream} iterator
     */
    @Override
    public Iterator<InputStream> iterator() {
        return fileNamesIterator;
    }

    /**
     * Iterate over each {@link InputStream} and apply the action on them.
     *
     * @param action to apply on {@link InputStream}.
     */
    @Override
    public void forEach(Consumer<? super InputStream> action) {
        DataSourceReader.super.forEach(action);
    }
}
