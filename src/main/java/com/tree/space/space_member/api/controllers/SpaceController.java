package com.tree.space.space_member.api.controllers;

import com.tree.space.space_member.api.dto.SpaceDTO;
import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.entities.Space;
import com.tree.space.space_member.core.entities.SpaceMember;
import com.tree.space.space_member.core.services.NodeService;
import com.tree.space.space_member.core.services.SpaceMemberService;
import com.tree.space.space_member.core.services.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;
    private final SpaceMemberService spaceMemberService;
    private final NodeService nodeService;

    @PostMapping
    public ResponseEntity<SpaceDTO> createSpace(@RequestBody SpaceDTO spaceDTO) {
        Space space = new Space();
        if (spaceDTO.getCreatorId() != null) {
            nodeService.getNodeById(spaceDTO.getCreatorId()).ifPresent(space::setCreator);
        }
        
        Space saved = spaceService.createSpace(space.getCreator().getId());
        return new ResponseEntity<>(convertToDTO(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceDTO> getSpace(@PathVariable Long id) {
        return spaceService.getSpaceById(id)
                .map(space -> ResponseEntity.ok(convertToDTO(space)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SpaceDTO>> getAllSpaces() {
        List<Space> spaces = spaceService.getAllSpaces();
        List<SpaceDTO> dtos = spaces.stream().map(SpaceController::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceDTO> updateSpace(@PathVariable Long id, @RequestBody SpaceDTO spaceDTO) {
        Space space = new Space();
        space.setId(id);
        if (spaceDTO.getCreatorId() != null) {
            nodeService.getNodeById(spaceDTO.getCreatorId()).ifPresent(space::setCreator);
        }
        if (spaceDTO.getSpaceMemberIds() != null) {
            Set<SpaceMember> members = spaceDTO.getSpaceMemberIds().stream()
                .map(mid -> spaceMemberService.getSpaceMemberById(mid).orElse(null))
                .filter(n -> n != null)
                .collect(Collectors.toSet());
            space.setMembers(members);
        }
        Space updated = spaceService.updateSpace(space);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpace(@PathVariable Long id) {
        spaceService.deleteSpace(id);
        return ResponseEntity.noContent().build();
    }

    public static SpaceDTO convertToDTO(Space space) {
        Long creatorId = space.getCreator() != null ? space.getCreator().getId() : null;
        Set<Long> memberIds = space.getMembers() != null ? space.getMembers().stream().map(SpaceMember::getId).collect(Collectors.toSet()) : null;
        return new SpaceDTO(space.getId(), creatorId, memberIds);
    }
} 