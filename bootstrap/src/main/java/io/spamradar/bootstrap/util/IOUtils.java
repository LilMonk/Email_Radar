package io.spamradar.bootstrap.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    // TODO: Triage about whether to keep these methods static or not.
    public static List<String> getAbsoluteFilePaths(String dirUrl) throws URISyntaxException {
        URI uri = new URI(dirUrl);
        File folder = new File(uri);
        return getAbsoluteFilePaths(folder);
    }

    public static List<String> getAbsoluteFilePaths(File folder) {
        File[] fileList = folder.listFiles();
        List<String> fileNames = new ArrayList<>();

        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile())
                    fileNames.add(file.getAbsolutePath());
                else if (file.isDirectory())
                    fileNames.addAll(getAbsoluteFilePaths(file));
            }
        }
        return fileNames;
    }

    public static InputStream getInputStreamFromFileUrl(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        return url.openStream();
    }
}
