package io.emailradar.commons.email.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CivilisedEmailNodeInfo implements Serializable {
    // Point to root node.
    private UUID rootId;

    // Point to next node in same level.
    private int nodePos;

    // Point to child nodes.
    private List<UUID> childNodeIds;

    // Node distance from root.
    private int nodeDepth;
}
