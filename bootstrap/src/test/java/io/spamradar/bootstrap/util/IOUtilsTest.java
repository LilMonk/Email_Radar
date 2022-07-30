package io.spamradar.bootstrap.util;

import org.junit.jupiter.api.Test;

import java.util.List;

public class IOUtilsTest {
    private String dir = "src/main/resources/emails";

    @Test
    void getFileNames() {
        List<String> fileNames = IOUtils.getAbsoluteFilePaths(dir);
        System.out.println(fileNames.size());
        for (String fileName : fileNames)
            System.out.println(fileName);
    }
}