package io.spamradar.bootstrap.util;

import io.spamradar.bootstrap.kafka.EmailProducer;
import io.spamradar.bootstrap.model.CivilisedEmail;
import io.spamradar.bootstrap.model.PrimitiveEmail;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
class CivilisedEmailParser2Test {
    @Value("${kafka.topic.name}")
    String topic;

    @Autowired
    EmailProducer emailProducer;

    @Test
    void emailParser2Test() throws MimeException, IOException {
        String emailFileName = "emails/spam/1.txt";
        InputStream inputStream = getFileFromResourceAsStream(emailFileName);
        EmailBuilder emailBuilder = new EmailBuilder();
        ContentHandler handler = new EmailContentHandler(emailBuilder);
        MimeStreamParser parser = new MimeStreamParser();
        parser.setContentHandler(handler);
        parser.parse(inputStream);

        PrimitiveEmail primitiveEmail = emailBuilder.build();
        PrimitiveToCivilisedEmailConverter primitiveToCivilisedEmailConverter = new PrimitiveToCivilisedEmailConverter(primitiveEmail);
        CivilisedEmail civilisedEmail = primitiveToCivilisedEmailConverter.getCivilisedEmail();
//        printPrimitiveEmail(primitiveEmail);
//        printCivilisedEmail(civilisedEmail);
        emailProducer.sendMessage(civilisedEmail, topic);
    }

    private void printCivilisedEmail(CivilisedEmail civilisedEmail) {
        if (civilisedEmail == null)
            return;
        System.out.println("Date: " + civilisedEmail.getDate());
        System.out.println("From: " + civilisedEmail.getFrom());
        System.out.println("To: " + civilisedEmail.getTo());
        System.out.println("Sub: " + civilisedEmail.getSubject());
        System.out.println("Content-Type: " + civilisedEmail.getContentType());
        System.out.println("Body: " + civilisedEmail.getBody());

        for (CivilisedEmail email : civilisedEmail.getChildEmails())
            printCivilisedEmail(email);
    }

    private void printPrimitiveEmail(PrimitiveEmail primitiveEmail) {
        if (primitiveEmail == null)
            return;

        System.out.println("Headers :: " + primitiveEmail.getHeaders());
        System.out.println("Body :: " + primitiveEmail.getBody());

        for (PrimitiveEmail email : primitiveEmail.getChildEmails()) {
            printPrimitiveEmail(email);
        }
    }


    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
}