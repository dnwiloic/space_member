package com.tree.space.space_member.core.services.impl;

import com.tree.space.space_member.core.entities.Discussion;
import com.tree.space.space_member.core.entities.Media;
import com.tree.space.space_member.core.entities.Message;
import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.entities.MessageReadStatus;
import com.tree.space.space_member.core.repositories.DiscussionRepository;
import com.tree.space.space_member.core.repositories.MediaRepository;
import com.tree.space.space_member.core.repositories.MessageReadStatusRepository;
import com.tree.space.space_member.core.repositories.MessageRepository;
import com.tree.space.space_member.core.repositories.NodeRepository;
import com.tree.space.space_member.core.services.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final DiscussionRepository discussionRepository;
    private final NodeRepository nodeRepository;
    private final MediaRepository mediaRepository;
    private final MessageReadStatusRepository messageReadStatusRepository;

    @Override
    public Message createMessage(Long discussionId, Long senderId, String content, Long replyToId) {
        Discussion discussion = discussionRepository.findById(discussionId)
            .orElseThrow(() -> new EntityNotFoundException("Discussion not found with id: " + discussionId));
        
        Node sender = nodeRepository.findById(senderId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + senderId));
        
        Message message = new Message();
        message.setContent(content);
        message.setSendDate(LocalDateTime.now());
        message.setSender(sender);
        message.setDiscussion(discussion);
        
        if (replyToId != null) {
            Message replyTo = messageRepository.findById(replyToId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + replyToId));
            message.setReplyTo(replyTo);
        }
        
        return messageRepository.save(message);
    }

    @Override
    public Message createMessageWithMedia(Long discussionId, Long senderId, String content, 
                                        Long replyToId, Set<Media> medias) {
        Message message = createMessage(discussionId, senderId, content, replyToId);
        if (medias != null && !medias.isEmpty()) {
            medias.forEach(media -> {
                media.setMessage(message);
                mediaRepository.save(media);
            });
            message.getMedias().addAll(medias);
        }
        return messageRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Message> getDiscussionMessages(Long discussionId, Pageable pageable) {
        Discussion discussion = discussionRepository.findById(discussionId)
            .orElseThrow(() -> new EntityNotFoundException("Discussion not found with id: " + discussionId));
        return messageRepository.findByDiscussionOrderBySendDateDesc(discussion, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getUserMessages(Long userId) {
        Node user = nodeRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + userId));
        return messageRepository.findBySender(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getReplies(Long messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + messageId));
        return messageRepository.findByReplyTo(message);
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public Message updateMessageContent(Long id, String newContent) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + id));
        message.setContent(newContent);
        return messageRepository.save(message);
    }

    @Override
    public Message addMediaToMessage(Long messageId, Media media) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + messageId));
        media.setMessage(message);
        mediaRepository.save(media);
        message.getMedias().add(media);
        return messageRepository.save(message);
    }

    @Override
    public Message removeMediaFromMessage(Long messageId, Long mediaId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + messageId));
        Media media = mediaRepository.findById(mediaId)
            .orElseThrow(() -> new EntityNotFoundException("Media not found with id: " + mediaId));
        
        message.getMedias().remove(media);
        mediaRepository.delete(media);
        return messageRepository.save(message);
    }

    @Override
    public MessageReadStatus markMessageAsRead(Long messageId, Long readerId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + messageId));
        Node reader = nodeRepository.findById(readerId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + readerId));
        
        MessageReadStatus status = messageReadStatusRepository
            .findByMessageAndReader(message, reader)
            .orElse(new MessageReadStatus());
        
        status.setMessage(message);
        status.setReader(reader);
        status.setRead(true);
        status.setReadDate(LocalDateTime.now());
        
        return messageReadStatusRepository.save(status);
    }

    @Override
    public MessageReadStatus markMessageAsUnread(Long messageId, Long readerId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + messageId));
        Node reader = nodeRepository.findById(readerId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + readerId));
        
        MessageReadStatus status = messageReadStatusRepository
            .findByMessageAndReader(message, reader)
            .orElse(new MessageReadStatus());
        
        status.setMessage(message);
        status.setReader(reader);
        status.setRead(false);
        status.setReadDate(null);
        
        return messageReadStatusRepository.save(status);
    }

    @Override
    public void markAllDiscussionMessagesAsRead(Long discussionId, Long readerId) {
        Discussion discussion = discussionRepository.findById(discussionId)
            .orElseThrow(() -> new EntityNotFoundException("Discussion not found with id: " + discussionId));
        Node reader = nodeRepository.findById(readerId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + readerId));
        
        List<Message> messages = messageRepository.findByDiscussionOrderBySendDateDesc(discussion, null).getContent();
        LocalDateTime now = LocalDateTime.now();
        
        messages.forEach(message -> {
            MessageReadStatus status = messageReadStatusRepository
                .findByMessageAndReader(message, reader)
                .orElse(new MessageReadStatus());
            
            status.setMessage(message);
            status.setReader(reader);
            status.setRead(true);
            status.setReadDate(now);
            
            messageReadStatusRepository.save(status);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isMessageReadByUser(Long messageId, Long readerId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + messageId));
        Node reader = nodeRepository.findById(readerId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + readerId));
        
        return messageReadStatusRepository
            .findByMessageAndReader(message, reader)
            .map(MessageReadStatus::isRead)
            .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadMessageCount(Long discussionId, Long readerId) {
        Discussion discussion = discussionRepository.findById(discussionId)
            .orElseThrow(() -> new EntityNotFoundException("Discussion not found with id: " + discussionId));
        Node reader = nodeRepository.findById(readerId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + readerId));
        
        return messageReadStatusRepository.countUnreadMessagesInDiscussion(discussion, reader);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getUnreadMessages(Long discussionId, Long readerId) {
        Discussion discussion = discussionRepository.findById(discussionId)
            .orElseThrow(() -> new EntityNotFoundException("Discussion not found with id: " + discussionId));
        Node reader = nodeRepository.findById(readerId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + readerId));
        
        List<MessageReadStatus> readStatuses = messageReadStatusRepository
            .findByDiscussionAndReader(discussion, reader);
        
        Set<Long> readMessageIds = readStatuses.stream()
            .filter(MessageReadStatus::isRead)
            .map(status -> status.getMessage().getId())
            .collect(Collectors.toSet());
        
        return messageRepository.findByDiscussionOrderBySendDateDesc(discussion, null)
            .getContent()
            .stream()
            .filter(message -> !readMessageIds.contains(message.getId()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageReadStatus> getMessageReadStatus(Long messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + messageId));
        return messageReadStatusRepository.findByMessage(message);
    }
} 