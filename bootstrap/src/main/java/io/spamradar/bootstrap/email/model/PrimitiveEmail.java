package io.spamradar.bootstrap.email.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This email class will contain key-value pairs of header, body and nested multipart emails.
 */
@Data
public class PrimitiveEmail {
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private List<PrimitiveEmail> childEmails = new ArrayList<>();
}
