package com.tree.space.space_member.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMessageRequest {
    @NotNull(message = "Discussion ID is required")
    private Long discussionId;
    
    @NotNull(message = "Sender ID is required")
    private Long senderId;
    
    @NotBlank(message = "Content cannot be empty")
    private String content;
    
    private Long replyToId;
} 