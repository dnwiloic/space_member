package com.tree.space.space_member.core.services;

import com.tree.space.space_member.core.entities.Message;
import com.tree.space.space_member.core.entities.Media;
import com.tree.space.space_member.core.entities.MessageReadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageService {
    
    Message createMessage(Long discussionId, Long senderId, String content, Long replyToId);
    
    Message createMessageWithMedia(Long discussionId, Long senderId, String content, Long replyToId, Set<Media> medias);
    
    Optional<Message> getMessageById(Long id);
    
    Page<Message> getDiscussionMessages(Long discussionId, Pageable pageable);
    
    List<Message> getUserMessages(Long userId);
    
    List<Message> getReplies(Long messageId);
    
    void deleteMessage(Long id);
    
    Message updateMessageContent(Long id, String newContent);
    
    Message addMediaToMessage(Long messageId, Media media);
    
    Message removeMediaFromMessage(Long messageId, Long mediaId);
    
    MessageReadStatus markMessageAsRead(Long messageId, Long readerId);
    
    MessageReadStatus markMessageAsUnread(Long messageId, Long readerId);
    
    void markAllDiscussionMessagesAsRead(Long discussionId, Long readerId);
    
    boolean isMessageReadByUser(Long messageId, Long readerId);
    
    long getUnreadMessageCount(Long discussionId, Long readerId);
    
    List<Message> getUnreadMessages(Long discussionId, Long readerId);
    
    List<MessageReadStatus> getMessageReadStatus(Long messageId);
} 