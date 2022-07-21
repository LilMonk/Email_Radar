package io.spamradar.bootstrap.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ListPagingUtil {
    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {

        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("Invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if (sourceList == null || sourceList.size() < fromIndex) {
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    public static <T> void doPaginated(Collection<T> fullList, Integer pageSize, Consumer<List<T>> pageInterface) {
        final List<T> list = new ArrayList<>(fullList);
        if (pageSize == null || pageSize <= 0 || pageSize > list.size()) {
            pageSize = list.size();
        }

        final int numPages = (int) Math.ceil((double) list.size() / (double) pageSize);
        for (int pageNum = 0; pageNum < numPages; ) {
            final List<T> page = list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size()));
            pageInterface.accept(page);
        }
    }
}
