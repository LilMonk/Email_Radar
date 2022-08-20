package io.emailradar.bootstrap.datasource.reader;

import io.emailradar.bootstrap.util.IOUtils;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

/**
 * Custom {@link Iterator} implementation for iterating over {@link InputStream} of file.
 */
public class FileDataSourceInputStream implements Iterator<InputStream> {
    private final boolean selfCloseInputStream;
    private final Iterator<String> fileNameIterator;
    private FileInputStream currentFileInputStream;


    /**
     * Custom {@link Iterator} implementation for iterating over {@link InputStream} of file.
     *
     * @param dirUrl              to read from.
     * @param selfCloseInputStream true if you want application to close the input stream, false otherwise.
     *                             In case of false, remember to close the input stream.
     */
    public FileDataSourceInputStream(String dirUrl, boolean selfCloseInputStream) throws URISyntaxException {
        List<String> fileNames = IOUtils.getAbsoluteFilePaths(dirUrl);
        this.selfCloseInputStream = selfCloseInputStream;
        this.fileNameIterator = fileNames.iterator();
    }

    /**
     * Check if next element is present in list of {@link InputStream} or not for the iterator to move.
     *
     * @return true if element is present, false otherwise.
     */
    @Override
    public boolean hasNext() {
        return fileNameIterator.hasNext();
    }

    /**
     * Gives the next {@link InputStream} in the list, if present.
     * Otherwise, throws an exception.
     *
     * @return next {@link InputStream} instance.
     */
    @Override
    @SneakyThrows
    public InputStream next() {
        if (!hasNext())
            throw new Exception("No elements remaining to be iterated.");

        if (selfCloseInputStream && this.currentFileInputStream != null)
            this.currentFileInputStream.close();

        this.currentFileInputStream = new FileInputStream(fileNameIterator.next());
        return this.currentFileInputStream;
    }
}
