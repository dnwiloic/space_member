package com.tree.space.space_member.api.controllers;

import com.tree.space.space_member.api.dto.NodeDTO;
import com.tree.space.space_member.api.dto.SpaceDTO;
import com.tree.space.space_member.api.dto.SpaceMemberDTO;
import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.entities.Space;
import com.tree.space.space_member.core.entities.SpaceMember;
import com.tree.space.space_member.core.services.SpaceMemberService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spaces/{spaceId}/members")
@RequiredArgsConstructor
public class SpaceMemberController {

    private final SpaceMemberService spaceMemberService;

    @PostMapping("/{nodeId}")
    public ResponseEntity<SpaceMemberDTO> addMember(
            @PathVariable Long spaceId,
            @PathVariable Long nodeId,
            @RequestParam(defaultValue = "false") boolean isAdmin) {
        SpaceMember member = spaceMemberService.addMember(spaceId, nodeId, isAdmin);
        return new ResponseEntity<>(SpaceMemberController.convertToDTO(member), HttpStatus.CREATED);
    }

    @DeleteMapping("/{nodeId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long spaceId,
            @PathVariable Long nodeId) {
        spaceMemberService.removeMember(spaceId, nodeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SpaceMemberDTO>> getMembers(@PathVariable Long spaceId) {
        List<SpaceMember> members = spaceMemberService.getMembers(spaceId);
        
        List<SpaceMemberDTO> dtos = members.stream().map(SpaceMemberController::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<SpaceMemberDTO>> getAdmins(@PathVariable Long spaceId) {
        List<SpaceMember> admins = spaceMemberService.getAdmins(spaceId);
        List<SpaceMemberDTO> dtos = admins.stream().map(SpaceMemberController::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{nodeId}/admin")
    public ResponseEntity<Void> updateAdminStatus(
            @PathVariable Long spaceId,
            @PathVariable Long nodeId,
            @RequestParam boolean isAdmin) {
        spaceMemberService.updateAdminStatus(spaceId, nodeId, isAdmin);
        return ResponseEntity.ok().build();
    }

    public static SpaceMemberDTO convertToDTO(SpaceMember spaceMember) {

        return new SpaceMemberDTO(
            spaceMember.getId(), 
            SpaceController.convertToDTO(spaceMember.getSpace()),
            NodeController.convertToDTO(spaceMember.getNode()),
            spaceMember.isAdmin() );
    }
}
