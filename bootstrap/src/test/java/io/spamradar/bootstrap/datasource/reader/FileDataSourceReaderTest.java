package io.spamradar.bootstrap.datasource.reader;

import io.spamradar.bootstrap.datasource.model.DataSource;
import io.spamradar.bootstrap.datasource.service.DataSourceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class FileDataSourceReaderTest {

    private DataSource getFileDataSource() throws URISyntaxException {
        String fileName = "/home/sentinel/Projects/Email_Spam_Detection/bootstrap/src/main/resources/emails/spam";
        return DataSourceProvider.getDataSourceInstance(fileName);
    }

    @Test
    void testFileReader() throws URISyntaxException {
        FileDataSourceReader fileDataSourceReader = new FileDataSourceReader(getFileDataSource(), true);
        List<String> emails = new ArrayList<>();
        fileDataSourceReader.forEach(ip -> {
            try {
                emails.add(readFromInputStream(ip));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Assertions.assertThat(emails.size()).isEqualTo(5);
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }
}