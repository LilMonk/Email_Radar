package io.spamradar.bootstrap.util;

import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    public static List<String> getAbsoluteFilePaths(String dirPath) {
        File folder = new File(dirPath);
        File[] fileList = folder.listFiles();
        List<String> fileNames = new ArrayList<>();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile())
                    fileNames.add(file.getAbsolutePath());
                else if (file.isDirectory())
                    fileNames.addAll(getAbsoluteFilePaths(file.getAbsolutePath()));
            }
        }
        return fileNames;
    }

    public InputStream getFileAsInputStream(String filePath) throws IOException {
        Resource fileResource = new FileUrlResource(filePath);
        return fileResource.getInputStream();
    }

    public InputStream getFileAsInputStream(URL fileUrl) throws IOException {
        Resource fileResource = new FileUrlResource(fileUrl);
        return fileResource.getInputStream();
    }
}
