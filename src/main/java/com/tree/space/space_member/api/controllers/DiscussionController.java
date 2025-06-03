package com.tree.space.space_member.api.controllers;

import com.tree.space.space_member.api.dto.DiscussionDTO;
import com.tree.space.space_member.core.entities.Discussion;
import com.tree.space.space_member.core.entities.EventDiscussion;
import com.tree.space.space_member.core.entities.GroupDiscussion;
import com.tree.space.space_member.core.entities.PeerToPeerDiscussion;
import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.services.DiscussionService;
import com.tree.space.space_member.core.services.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/discussions")
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;
    private final NodeService nodeService;

    @PostMapping("/group")
    public ResponseEntity<DiscussionDTO> createGroupDiscussion(@RequestParam Long spaceId) {
        GroupDiscussion discussion = discussionService.createGroupDiscussion(spaceId);
        return new ResponseEntity<>(convertToDTO(discussion), HttpStatus.CREATED);
    }

    @PostMapping("/event")
    public ResponseEntity<DiscussionDTO> createEventDiscussion(@RequestParam Long spaceId, @RequestParam Long eventId) {
        EventDiscussion discussion = discussionService.createEventDiscussion(spaceId, eventId);
        return new ResponseEntity<>(convertToDTO(discussion), HttpStatus.CREATED);
    }

    @PostMapping("/p2p")
    public ResponseEntity<DiscussionDTO> createP2PDiscussion(@RequestParam Long spaceId, @RequestBody Set<Long> participantIds) {
        PeerToPeerDiscussion discussion = discussionService.createP2PDiscussion(spaceId, participantIds.stream().toList());
        return new ResponseEntity<>(convertToDTO(discussion), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscussionDTO> getDiscussion(@PathVariable Long id) {
        return discussionService.getDiscussionById(id)
                .map(discussion -> ResponseEntity.ok(convertToDTO(discussion)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/space/{spaceId}")
    public ResponseEntity<List<DiscussionDTO>> getDiscussionsBySpace(@PathVariable Long spaceId) {
        List<Discussion> discussions = discussionService.getDiscussionsBySpace(spaceId);
        List<DiscussionDTO> dtos = discussions.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Gestion des participants P2P
    @PostMapping("/{discussionId}/participants/{nodeId}")
    public ResponseEntity<Void> addP2PParticipant(@PathVariable Long discussionId, @PathVariable Long nodeId) {
        discussionService.addP2PParticipant(discussionId, nodeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{discussionId}/participants/{nodeId}")
    public ResponseEntity<Void> removeP2PParticipant(@PathVariable Long discussionId, @PathVariable Long nodeId) {
        discussionService.removeP2PParticipant(discussionId, nodeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{discussionId}/participants")
    public ResponseEntity<Set<Long>> getP2PParticipants(@PathVariable Long discussionId) {
        List<Node> participants = discussionService.getP2PParticipants(discussionId);
        Set<Long> ids = participants.stream().map(Node::getId).collect(Collectors.toSet());
        return ResponseEntity.ok(ids);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscussion(@PathVariable Long id) {
        discussionService.deleteDiscussion(id);
        return ResponseEntity.noContent().build();
    }

    private DiscussionDTO convertToDTO(Discussion discussion) {
        String type = null;
        Long eventId = null;
        Set<Long> participantIds = null;
        if (discussion instanceof GroupDiscussion) {
            type = "GROUP";
        } else if (discussion instanceof EventDiscussion ed) {
            type = "EVENT";
            eventId = ed.getEvent() != null ? ed.getEvent().getId() : null;
        } else if (discussion instanceof PeerToPeerDiscussion p2p) {
            type = "P2P";
            participantIds = p2p.getParticipants() != null ? p2p.getParticipants().stream().map(Node::getId).collect(Collectors.toSet()) : new HashSet<>();
        }
        return new DiscussionDTO(discussion.getId(), type, discussion.getSpace().getId(), eventId, participantIds);
    }
} 