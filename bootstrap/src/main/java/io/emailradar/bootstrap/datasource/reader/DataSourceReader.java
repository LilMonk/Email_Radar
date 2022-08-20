package io.emailradar.bootstrap.datasource.reader;

import java.io.InputStream;

/**
 * Extends an {@link Iterable<InputStream>} for creating an iterable data source.
 */
public interface DataSourceReader extends Iterable<InputStream> {
}
