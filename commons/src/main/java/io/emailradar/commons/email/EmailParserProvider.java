package io.emailradar.commons.email;

import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.MimeConfig;

public class EmailParserProvider {
    public static EmailParser getInstance() {
        EmailBuilder emailBuilder = new EmailBuilder();
        EmailContentHandler contentHandler = new EmailContentHandler(emailBuilder);
        MimeStreamParser mimeStreamParser = new MimeStreamParser(MimeConfig.PERMISSIVE);
        mimeStreamParser.setContentHandler(contentHandler);
        return new EmailParser(mimeStreamParser, emailBuilder);
    }
}
