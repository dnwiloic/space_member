package com.tree.space.space_member.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String content;
    private LocalDateTime sendDate;
    private Long senderId;
    private String senderName;
    private Long discussionId;
    private Long replyToId;
    private Set<MediaDTO> medias = new HashSet<>();
    private boolean read;
    private LocalDateTime readDate;
} 