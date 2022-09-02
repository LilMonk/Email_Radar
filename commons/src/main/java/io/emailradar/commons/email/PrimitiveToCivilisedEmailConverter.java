package io.emailradar.commons.email;

import io.emailradar.commons.email.mapper.EmailTreeNode;
import io.emailradar.commons.email.model.CivilisedEmail;
import io.emailradar.commons.email.model.CivilisedEmailHeader;
import io.emailradar.commons.email.model.CivilisedEmailNodeInfo;
import io.emailradar.commons.email.model.PrimitiveEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.apache.james.mime4j.dom.field.FieldName.*;

/**
 * Converts the {@link PrimitiveEmail} instance to {@link CivilisedEmail} instance.
 */
@Slf4j
@Component
public class PrimitiveToCivilisedEmailConverter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, d MMM yyyy HH:mm:ss Z");

    /**
     * Converts {@link PrimitiveEmail} instance to {@link List<CivilisedEmail>} instance.
     *
     * @param primitiveEmail instance to be converted.
     * @return {@link List<CivilisedEmail>} instance.
     */
    public List<CivilisedEmail> convertToCivilisedEmail(PrimitiveEmail primitiveEmail) {
        EmailTreeNode<CivilisedEmail> emailTreeNode = generateEmailTree(primitiveEmail);
        return flattenEmailTree(emailTreeNode);
    }

    private CivilisedEmailHeader getCivilisedEmailHeaderFrom(PrimitiveEmail primitiveEmail) {
        Map<String, String> primitiveEmailHeader = primitiveEmail.getHeaders();
        String dateStr = primitiveEmailHeader.getOrDefault(DATE, null);
        String from = primitiveEmailHeader.getOrDefault(FROM, null);
        String toStr = primitiveEmailHeader.getOrDefault(TO, null);
        String contentType = primitiveEmailHeader.getOrDefault(CONTENT_TYPE, null);
        String contentTransferEncoding = primitiveEmailHeader.getOrDefault(CONTENT_TRANSFER_ENCODING, null);
        String subject = primitiveEmailHeader.getOrDefault(SUBJECT, null);
        ZonedDateTime date = parseDateTime(dateStr);
        List<String> to = toStr != null ? List.of(toStr.split(", ")) : null;

        return CivilisedEmailHeader.builder()
                .date(date)
                .from(from)
                .to(to)
                .contentType(contentType)
                .contentTransferEncoding(contentTransferEncoding)
                .subject(subject)
                .build();
    }

    private ZonedDateTime parseDateTime(String dateTime) {
        if (dateTime == null)
            return null;

        ZonedDateTime zonedDateTime = null;
        try {
            zonedDateTime = ZonedDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException dateTimeParseException) {
            log.error(dateTimeParseException.toString());
        }

        return zonedDateTime;
    }

    private EmailTreeNode<CivilisedEmail> generateEmailTree(PrimitiveEmail primitiveEmail) {
        CivilisedEmailHeader civilisedEmailHeader = getCivilisedEmailHeaderFrom(primitiveEmail);
        String body = primitiveEmail.getBody();
        CivilisedEmail civilisedEmail = CivilisedEmail.builder()
                ._id()
                .header(civilisedEmailHeader)
                .nodeInfo(null)
                .body(body)
                .build();

        List<EmailTreeNode<CivilisedEmail>> childNodes = new ArrayList<>();
        for (PrimitiveEmail pEmail : primitiveEmail.getChildEmails()) {
            EmailTreeNode<CivilisedEmail> node = generateEmailTree(pEmail);
            childNodes.add(node);
        }
        return new EmailTreeNode<>(civilisedEmail, childNodes);
    }

    private List<CivilisedEmail> flattenEmailTree(EmailTreeNode<CivilisedEmail> emailTreeNode) {
        CivilisedEmail civilisedEmail = emailTreeNode.getNode();
        return flattenEmailTree(emailTreeNode, civilisedEmail.get_id(), 0, 0, new ArrayList<>());
    }

    private List<CivilisedEmail> flattenEmailTree(EmailTreeNode<CivilisedEmail> emailTreeNode,
                                                  UUID rootId,
                                                  int nodePos,
                                                  int nodeDepth,
                                                  List<CivilisedEmail> civilisedEmails
    ) {
        CivilisedEmail civilisedEmail = emailTreeNode.getNode();
        List<UUID> childNodeIds = emailTreeNode.getChildNodes()
                .stream()
                .map(node -> node.getNode().get_id())
                .toList();

        CivilisedEmailNodeInfo civilisedEmailNodeInfo = CivilisedEmailNodeInfo.builder()
                .rootId(rootId)
                .nodePos(nodePos)
                .childNodeIds(childNodeIds)
                .nodeDepth(nodeDepth)
                .build();

        civilisedEmail.setNodeInfo(civilisedEmailNodeInfo);
        civilisedEmails.add(civilisedEmail);

        List<EmailTreeNode<CivilisedEmail>> emailTreeNodes = emailTreeNode.getChildNodes();
        for (int i = 0; i < emailTreeNodes.size(); i++) {
            flattenEmailTree(emailTreeNodes.get(i), rootId, i, nodeDepth + 1, civilisedEmails);
        }

        return civilisedEmails;
    }
}
