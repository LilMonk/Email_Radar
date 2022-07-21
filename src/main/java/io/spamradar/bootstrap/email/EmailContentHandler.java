package io.spamradar.bootstrap.email;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class EmailContentHandler implements ContentHandler {
    private final EmailBuilder emailBuilder;

    public EmailContentHandler(EmailBuilder emailBuilder) {
        this.emailBuilder = emailBuilder;
    }

    /**
     * Called when a new message starts (a top level message or an embedded
     * rfc822 message).
     *
     * @throws MimeException on processing errors
     */
    @SneakyThrows
    @Override
    public void startMessage() throws MimeException {
        emailBuilder.startEmail();
        log.debug("New Message Started!!!");
    }

    /**
     * Called when a message ends.
     *
     * @throws MimeException on processing errors
     */
    @SneakyThrows
    @Override
    public void endMessage() throws MimeException {
        emailBuilder.endEmail();
        log.debug("Message Ended!!!");
    }

    /**
     * Called when a new body part starts inside a
     * <code>multipart/*</code> entity.
     *
     * @throws MimeException on processing errors
     */
    @SneakyThrows
    @Override
    public void startBodyPart() throws MimeException {
        emailBuilder.startMessage();
        log.debug("New Body Part Started!!!");
    }

    /**
     * Called when a body part ends.
     *
     * @throws MimeException on processing errors
     */
    @SneakyThrows
    @Override
    public void endBodyPart() throws MimeException {
        emailBuilder.endMessage();
        log.debug("Body Part Ended!!!");
    }

    /**
     * Called when a header (of a message or body part) is about to be parsed.
     *
     * @throws MimeException on processing errors
     */
    @Override
    public void startHeader() throws MimeException {
        log.debug("New Header Started!!!");
    }

    /**
     * Called for each field of a header.
     *
     * @param rawField the MIME field.
     * @throws MimeException on processing errors
     */
    @Override
    public void field(Field rawField) throws MimeException {
        emailBuilder.insertHeader(rawField);
        log.debug("Header field detected: {} = {}", rawField.getName(), rawField.getBody());
    }

    /**
     * Called when there are no more header fields in a message or body part.
     *
     * @throws MimeException on processing errors
     */
    @Override
    public void endHeader() throws MimeException {
        log.debug("Header Ended!!!");
    }

    /**
     * Called for the preamble (whatever comes before the first body part)
     * of a <code>multipart/*</code> entity.
     *
     * @param is used to get the contents of the preamble.
     * @throws MimeException on processing errors
     * @throws IOException   should be thrown on I/O errors.
     */
    @Override
    public void preamble(InputStream is) throws MimeException, IOException {
        log.debug("Preamble: {}", readFromInputStream(is));
    }

    /**
     * Called for the epilogue (whatever comes after the final body part)
     * of a <code>multipart/*</code> entity.
     *
     * @param is used to get the contents of the epilogue.
     * @throws MimeException on processing errors
     * @throws IOException   should be thrown on I/O errors.
     */
    @Override
    public void epilogue(InputStream is) throws MimeException, IOException {
        log.debug("Epilogue: {}" + readFromInputStream(is));
    }

    /**
     * Called when the body of a multipart entity is about to be parsed.
     *
     * @param bd encapsulates the values (either read from the
     *           message stream or, if not present, determined implictly
     *           as described in the
     *           MIME rfc:s) of the <code>Content-Type</code> and
     *           <code>Content-Transfer-Encoding</code> header fields.
     * @throws MimeException on processing errors
     */
    @Override
    public void startMultipart(BodyDescriptor bd) throws MimeException {
        log.debug("Multipart message detected, header data = {}", bd);
    }

    /**
     * Called when the body of an entity has been parsed.
     *
     * @throws MimeException on processing errors
     */
    @Override
    public void endMultipart() throws MimeException {
        log.debug("Multipart message ended!!!");
    }

    /**
     * Called when the body of a discrete (non-multipart) entity is about to
     * be parsed.
     *
     * @param bd see {@link #startMultipart(BodyDescriptor)}
     * @param is the contents of the body. NOTE: this is the raw body contents
     *           - it will not be decoded if encoded. The <code>bd</code>
     *           parameter should be used to determine how the stream data
     *           should be decoded.
     * @throws MimeException on processing errors
     * @throws IOException   should be thrown on I/O errors.
     */
    @Override
    public void body(BodyDescriptor bd, InputStream is) throws MimeException, IOException {
        String bodyData = readFromInputStream(is);
        emailBuilder.insertBody(bodyData);
        log.debug("Body detected, contents = {}, header data = {}", bodyData, bd);
    }

    /**
     * Called when a new entity (message or body part) starts and the
     * parser is in <code>raw</code> mode.
     *
     * @param is the raw contents of the entity.
     * @throws MimeException on processing errors
     * @throws IOException   should be thrown on I/O errors.
     * @see MimeStreamParser#setRaw()
     */
    @Override
    public void raw(InputStream is) throws MimeException, IOException {
    }

    /**
     * Read the data from inputStream and return it in string format.
     *
     * @param inputStream Incoming inputStream to read from.
     * @return Data in string format.
     * @throws IOException on error while reading from inputStream,
     */
    private String readFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }
}
