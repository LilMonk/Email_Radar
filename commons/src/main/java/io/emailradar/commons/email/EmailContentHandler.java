package io.emailradar.commons.email;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
class EmailContentHandler implements ContentHandler {
    private final EmailBuilder emailBuilder;

    public EmailContentHandler(EmailBuilder emailBuilder) {
        this.emailBuilder = emailBuilder;
    }

    /**
     * Called when a new message starts (a top level message or an embedded
     * rfc822 message).
     */
    @SneakyThrows
    @Override
    public void startMessage() {
        emailBuilder.startEmail();
        log.debug("New Message Started!!!");
    }

    /**
     * Called when a message ends.
     */
    @SneakyThrows
    @Override
    public void endMessage() {
        emailBuilder.endEmail();
        log.debug("Message Ended!!!");
    }

    /**
     * Called when a new body part starts inside a
     * <code>multipart/*</code> entity.
     */
    @SneakyThrows
    @Override
    public void startBodyPart() {
        emailBuilder.startMessage();
        log.debug("New Body Part Started!!!");
    }

    /**
     * Called when a body part ends.
     */
    @SneakyThrows
    @Override
    public void endBodyPart() {
        emailBuilder.endMessage();
        log.debug("Body Part Ended!!!");
    }

    /**
     * Called when a header (of a message or body part) is about to be parsed.
     */
    @Override
    public void startHeader() {
        log.debug("New Header Started!!!");
    }

    /**
     * Called for each field of a header.
     *
     * @param rawField the MIME field.
     */
    @Override
    public void field(Field rawField) {
        emailBuilder.insertHeader(rawField);
        log.debug("Header field detected: {} = {}", rawField.getName(), rawField.getBody());
    }

    /**
     * Called when there are no more header fields in a message or body part.
     */
    @Override
    public void endHeader() {
        log.debug("Header Ended!!!");
    }

    /**
     * Called for the preamble (whatever comes before the first body part)
     * of a <code>multipart/*</code> entity.
     *
     * @param is used to get the contents of the preamble.
     * @throws IOException should be thrown on I/O errors.
     */
    @Override
    public void preamble(InputStream is) throws IOException {
        log.debug("Preamble: {}", readFromInputStream(is));
    }

    /**
     * Called for the epilogue (whatever comes after the final body part)
     * of a <code>multipart/*</code> entity.
     *
     * @param is used to get the contents of the epilogue.
     * @throws IOException should be thrown on I/O errors.
     */
    @Override
    public void epilogue(InputStream is) throws IOException {
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
     */
    @Override
    public void startMultipart(BodyDescriptor bd) {
        log.debug("Multipart message detected, header data = {}", bd);
    }

    /**
     * Called when the body of an entity has been parsed.
     */
    @Override
    public void endMultipart() {
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
     * @throws IOException should be thrown on I/O errors.
     */
    @Override
    public void body(BodyDescriptor bd, InputStream is) throws IOException {
        String bodyData = readFromInputStream(is);
        emailBuilder.insertBody(bodyData);
        log.debug("Body detected, contents = {}, header data = {}", bodyData, bd);
    }

    /**
     * Called when a new entity (message or body part) starts and the
     * parser is in <code>raw</code> mode.
     *
     * @param is the raw contents of the entity.
     * @see MimeStreamParser#setRaw()
     */
    @Override
    public void raw(InputStream is) {
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
        return result.toString(StandardCharsets.UTF_8);
    }
}
