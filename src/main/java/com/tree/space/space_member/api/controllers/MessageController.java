package com.tree.space.space_member.api.controllers;

import com.tree.space.space_member.api.dto.CreateMessageRequest;
import com.tree.space.space_member.api.dto.MessageDTO;
import com.tree.space.space_member.core.entities.Message;
import com.tree.space.space_member.core.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@Valid @RequestBody CreateMessageRequest request) {
        Message message = messageService.createMessage(
            request.getDiscussionId(),
            request.getSenderId(),
            request.getContent(),
            request.getReplyToId()
        );
        return new ResponseEntity<>(convertToDTO(message), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getMessage(@PathVariable Long id) {
        return messageService.getMessageById(id)
            .map(message -> ResponseEntity.ok(convertToDTO(message)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/discussion/{discussionId}")
    public ResponseEntity<Page<MessageDTO>> getDiscussionMessages(
            @PathVariable Long discussionId,
            Pageable pageable) {
        Page<Message> messages = messageService.getDiscussionMessages(discussionId, pageable);
        Page<MessageDTO> messageDTOs = messages.map(this::convertToDTO);
        return ResponseEntity.ok(messageDTOs);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageDTO>> getUserMessages(@PathVariable Long userId) {
        List<Message> messages = messageService.getUserMessages(userId);
        List<MessageDTO> messageDTOs = messages.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    @GetMapping("/{messageId}/replies")
    public ResponseEntity<List<MessageDTO>> getMessageReplies(@PathVariable Long messageId) {
        List<Message> replies = messageService.getReplies(messageId);
        List<MessageDTO> replyDTOs = replies.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(replyDTOs);
    }

    @PutMapping("/{id}/content")
    public ResponseEntity<MessageDTO> updateMessageContent(
            @PathVariable Long id,
            @RequestBody String newContent) {
        Message updatedMessage = messageService.updateMessageContent(id, newContent);
        return ResponseEntity.ok(convertToDTO(updatedMessage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{messageId}/read")
    public ResponseEntity<Void> markMessageAsRead(
            @PathVariable Long messageId,
            @RequestParam Long readerId) {
        messageService.markMessageAsRead(messageId, readerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{messageId}/unread")
    public ResponseEntity<Void> markMessageAsUnread(
            @PathVariable Long messageId,
            @RequestParam Long readerId) {
        messageService.markMessageAsUnread(messageId, readerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/discussion/{discussionId}/read-all")
    public ResponseEntity<Void> markAllDiscussionMessagesAsRead(
            @PathVariable Long discussionId,
            @RequestParam Long readerId) {
        messageService.markAllDiscussionMessagesAsRead(discussionId, readerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{messageId}/read-status")
    public ResponseEntity<Boolean> isMessageReadByUser(
            @PathVariable Long messageId,
            @RequestParam Long readerId) {
        boolean isRead = messageService.isMessageReadByUser(messageId, readerId);
        return ResponseEntity.ok(isRead);
    }

    @GetMapping("/discussion/{discussionId}/unread-count")
    public ResponseEntity<Long> getUnreadMessageCount(
            @PathVariable Long discussionId,
            @RequestParam Long readerId) {
        long count = messageService.getUnreadMessageCount(discussionId, readerId);
        return ResponseEntity.ok(count);
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setSendDate(message.getSendDate());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getPseudo());
        dto.setDiscussionId(message.getDiscussion().getId());
        if (message.getReplyTo() != null) {
            dto.setReplyToId(message.getReplyTo().getId());
        }
        // Conversion des médias si nécessaire
        return dto;
    }
} 