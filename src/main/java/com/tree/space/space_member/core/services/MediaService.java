package com.tree.space.space_member.core.services;

import com.tree.space.space_member.core.entities.Media;
import java.util.List;
import java.util.Optional;

public interface MediaService {
    
    Media createMedia(Long messageId, String path, String type);
    
    Optional<Media> getMediaById(Long id);
    
    List<Media> getMessageMedias(Long messageId);
    
    List<Media> getMediasByType(String type);
    
    void deleteMedia(Long id);
    
    void deleteMessageMedias(Long messageId);
    
    Media updateMediaPath(Long id, String newPath);
    
    Media updateMediaType(Long id, String newType);
} 