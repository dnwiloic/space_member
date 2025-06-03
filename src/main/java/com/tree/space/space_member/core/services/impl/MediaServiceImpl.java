package com.tree.space.space_member.core.services.impl;

import com.tree.space.space_member.core.entities.Media;
import com.tree.space.space_member.core.entities.Message;
import com.tree.space.space_member.core.repositories.MediaRepository;
import com.tree.space.space_member.core.repositories.MessageRepository;
import com.tree.space.space_member.core.services.MediaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final MessageRepository messageRepository;

    @Override
    public Media createMedia(Long messageId, String path, String type) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + messageId));
        
        Media media = new Media();
        media.setPath(path);
        media.setType(type);
        media.setMessage(message);
        
        return mediaRepository.save(media);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Media> getMediaById(Long id) {
        return mediaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Media> getMessageMedias(Long messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + messageId));
        return mediaRepository.findByMessage(message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Media> getMediasByType(String type) {
        return mediaRepository.findByType(type);
    }

    @Override
    public void deleteMedia(Long id) {
        mediaRepository.deleteById(id);
    }

    @Override
    public void deleteMessageMedias(Long messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + messageId));
        mediaRepository.deleteByMessage(message);
    }

    @Override
    public Media updateMediaPath(Long id, String newPath) {
        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Media not found with id: " + id));
        media.setPath(newPath);
        return mediaRepository.save(media);
    }

    @Override
    public Media updateMediaType(Long id, String newType) {
        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Media not found with id: " + id));
        media.setType(newType);
        return mediaRepository.save(media);
    }
} 