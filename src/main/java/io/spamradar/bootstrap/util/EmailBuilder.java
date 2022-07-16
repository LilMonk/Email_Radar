package io.spamradar.bootstrap.util;

import io.spamradar.bootstrap.model.PrimitiveEmail;
import org.apache.james.mime4j.stream.Field;

import java.util.Stack;

/**
 * This class will create a tree of emails with {@link PrimitiveEmail} as its nodes.
 * An instance of this class should be provided to {@link EmailContentHandler} so that
 * whenever some event happens like a message start or header field found then the respective
 * methods should be invoked to construct the tree.
 */
public class EmailBuilder {
    private final Stack<PrimitiveEmail> primitiveEmailStack;
    private PrimitiveEmail root;

    private PrimitiveEmail currentNode;

    public EmailBuilder() {
        this.primitiveEmailStack = new Stack<>();
        this.root = null;
        this.currentNode = null;
    }

    /**
     * When a new message start or a multipart message is started this method is
     * invoked. This will create a new node so that respective header and body
     * should be saved in particular node.
     */
    void startMessage() {
        if (root == null) {
            this.root = new PrimitiveEmail();
            this.currentNode = root;
        } else {
            PrimitiveEmail newNode = new PrimitiveEmail();
            currentNode.getChildEmails().add(newNode);
            primitiveEmailStack.push(currentNode);
            currentNode = newNode;
        }
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
     * At the end of the message this method should be invoked to mark the completion
     * of data filling inside the node.
     */
    void endMessage() {
        currentNode = !primitiveEmailStack.isEmpty() ? primitiveEmailStack.pop() : null;
    }

    /**
     * Returns the constructed {@link PrimitiveEmail} instance from the raw email text.
     *
     * @return constructed email.
     */
    public PrimitiveEmail build() {
        return root;
    }
}
