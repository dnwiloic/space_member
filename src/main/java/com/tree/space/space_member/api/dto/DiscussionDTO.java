package com.tree.space.space_member.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionDTO {
    private Long id;
    private String type; // GROUP, EVENT, P2P
    private Long spaceId;
    private Long eventId; // pour EventDiscussion
    private Set<Long> participantIds; // pour P2P
} 