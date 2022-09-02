package io.emailradar.commons.email;

import io.emailradar.commons.email.model.PrimitiveEmail;
import io.emailradar.commons.exception.EmailParseException;
import lombok.AllArgsConstructor;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@AllArgsConstructor
public class EmailParser {
    @Autowired
    private MimeStreamParser parser;
    @Autowired
    private EmailBuilder emailBuilder;

    /**
     * Parse an email from the given input stream.
     *
     * @param inputStream email input stream.
     * @return parsed email instance.
     * @throws MimeException when email is malformed or have invalid data.
     * @throws IOException   when error occurs in input stream.
     */
    public PrimitiveEmail parse(InputStream inputStream) throws MimeException, IOException, EmailParseException {
        PrimitiveEmail email = new PrimitiveEmail();
        emailBuilder.register(email);
        parser.parse(inputStream);
        return emailBuilder.build();
    }
}