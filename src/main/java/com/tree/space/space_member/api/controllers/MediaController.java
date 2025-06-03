package com.tree.space.space_member.api.controllers;

import com.tree.space.space_member.api.dto.MediaDTO;
import com.tree.space.space_member.core.entities.Media;
import com.tree.space.space_member.core.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity<MediaDTO> createMedia(
            @RequestParam Long messageId,
            @RequestParam String path,
            @RequestParam String type) {
        Media media = mediaService.createMedia(messageId, path, type);
        return new ResponseEntity<>(convertToDTO(media), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaDTO> getMedia(@PathVariable Long id) {
        return mediaService.getMediaById(id)
            .map(media -> ResponseEntity.ok(convertToDTO(media)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/message/{messageId}")
    public ResponseEntity<List<MediaDTO>> getMessageMedias(@PathVariable Long messageId) {
        List<Media> medias = mediaService.getMessageMedias(messageId);
        List<MediaDTO> mediaDTOs = medias.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(mediaDTOs);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<MediaDTO>> getMediasByType(@PathVariable String type) {
        List<Media> medias = mediaService.getMediasByType(type);
        List<MediaDTO> mediaDTOs = medias.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(mediaDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/message/{messageId}")
    public ResponseEntity<Void> deleteMessageMedias(@PathVariable Long messageId) {
        mediaService.deleteMessageMedias(messageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/path")
    public ResponseEntity<MediaDTO> updateMediaPath(
            @PathVariable Long id,
            @RequestBody String newPath) {
        Media updatedMedia = mediaService.updateMediaPath(id, newPath);
        return ResponseEntity.ok(convertToDTO(updatedMedia));
    }

    @PutMapping("/{id}/type")
    public ResponseEntity<MediaDTO> updateMediaType(
            @PathVariable Long id,
            @RequestBody String newType) {
        Media updatedMedia = mediaService.updateMediaType(id, newType);
        return ResponseEntity.ok(convertToDTO(updatedMedia));
    }

    private MediaDTO convertToDTO(Media media) {
        MediaDTO dto = new MediaDTO();
        dto.setId(media.getId());
        dto.setPath(media.getPath());
        dto.setType(media.getType());
        dto.setMessageId(media.getMessage().getId());
        return dto;
    }
} 