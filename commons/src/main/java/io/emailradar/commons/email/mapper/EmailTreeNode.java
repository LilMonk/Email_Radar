package io.emailradar.commons.email.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmailTreeNode<T> {
    private T node;
    private List<EmailTreeNode<T>> childNodes;
}
