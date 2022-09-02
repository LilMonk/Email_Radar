package io.emailradar.commons.email;

import io.emailradar.commons.email.model.PrimitiveEmail;
import io.emailradar.commons.exception.EmailParseException;
import org.apache.james.mime4j.stream.Field;
import org.springframework.stereotype.Component;

import java.util.Stack;

/**
 * This class will create a tree of emails with {@link PrimitiveEmail} as its nodes.
 * An instance of this class should be provided to {@link EmailContentHandler} so that
 * whenever some event happens like a message start or header field found then the respective
 * methods should be invoked to construct the tree.
 */
@Component
public class EmailBuilder {
    private final Stack<PrimitiveEmail> primitiveEmailStack;
    private PrimitiveEmail root;
    private PrimitiveEmail currentNode;
    private PrimitiveEmail registeredNode;

    public EmailBuilder() {
        this.primitiveEmailStack = new Stack<>();
        this.root = null;
        this.currentNode = null;
        this.registeredNode = null;
    }

    /**
     * Returns the constructed {@link PrimitiveEmail} instance from the raw email text.
     *
     * @return constructed email.
     */
    public PrimitiveEmail build() {
        return root;
    }

    /**
     * Always run this method to register a {@link PrimitiveEmail} instance before
     * parsing an email.
     *
     * @param email root node in which the data will be filled.
     */
    public void register(PrimitiveEmail email) throws EmailParseException {
        if(this.registeredNode != null)
            throw new EmailParseException("Parsing of the current email is not finished.");
        this.registeredNode = email;
        reset();
    }

    /**
     * Reset the state of EmailBuilder.
     */
    void reset() {
        this.primitiveEmailStack.clear();
        this.root = null;
        this.currentNode = null;
    }

    /**
     * At the start of the email this method should be invoked to mark the
     * starting of email. This also checks the validity of registered node.
     *
     * @throws EmailParseException if a {@link PrimitiveEmail} instance is not registered.
     */
    void startEmail() throws EmailParseException {
        if (this.registeredNode == null)
            throw new EmailParseException("Register a PrimitiveEmail instance before parsing an email.");
        startMessage();
    }

    /**
     * When a new message body start or a multipart message is started this method is
     * invoked. This will create a new node so that respective header and body
     * should be saved in particular node.
     */
    void startMessage() {
        if (root == null) {
            this.root = this.registeredNode;
            this.currentNode = this.root;
        } else {
            PrimitiveEmail newNode = new PrimitiveEmail();
            newNode.getHeaders().putAll(currentNode.getHeaders());
            currentNode.getChildEmails().add(newNode);
            currentNode = newNode;
        }
        primitiveEmailStack.push(currentNode);
    }

    /**
     * Insert the key-value pair of header fields in the node.
     *
     * @param field header field.
     */
    void insertHeader(Field field) {
        currentNode.getHeaders().put(field.getName(), field.getBody());
    }

    /**
     * Insert the message body in the node.
     *
     * @param body message body.
     */
    void insertBody(String body) {
        currentNode.setBody(body);
    }

    /**
     * At the end of the message body this method should be invoked to mark the completion
     * of data filling inside the node.
     *
     * @throws EmailParseException when email structure is malformed.
     */
    void endMessage() throws EmailParseException {
        if (primitiveEmailStack.isEmpty())
            throw new EmailParseException("Malformed email structure");
        primitiveEmailStack.pop();
        currentNode = !primitiveEmailStack.isEmpty() ? primitiveEmailStack.peek() : null;
    }

    /**
     * At the end of email this method should be invoked to mark the completion
     * of email.
     *
     * @throws EmailParseException when email structure is malformed.
     */
    void endEmail() throws EmailParseException {
        endMessage();
        if (!primitiveEmailStack.empty())
            throw new EmailParseException("Malformed email structure");

        this.currentNode = null;

        // Reset the registeredNode
        this.registeredNode = null;
    }
}
