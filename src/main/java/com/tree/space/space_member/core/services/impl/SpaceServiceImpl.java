package com.tree.space.space_member.core.services.impl;

import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.entities.Space;
import com.tree.space.space_member.core.repositories.NodeRepository;
import com.tree.space.space_member.core.repositories.SpaceRepository;
import com.tree.space.space_member.core.services.SpaceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository spaceRepository;
    private final NodeRepository nodeRepository;
    private final SpaceMemberServiceImpl spaceMemberService;

    @Override
    public Space createSpace(Long creatorId) {
        Node creator = nodeRepository.findById(creatorId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + creatorId));
        Space space = new Space();
        space.setCreator(creator);

        Space createdSpace = spaceRepository.save(space);
        
        // Add creator as a member
        spaceMemberService.addMember(createdSpace.getId(), createdSpace.getCreator().getId(), true);
    
        return createdSpace;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Space> getAllSpaces(){
        return spaceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Space> getSpaceById(Long id) {
        return spaceRepository.findById(id);
    }

    // @Override
    // public Space addMember(Long spaceId, Long nodeId) {
    //     Space space = spaceRepository.findById(spaceId)
    //         .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
    //     Node node = nodeRepository.findById(nodeId)
    //         .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        
    //     spaceMemberService.addMember(node.getId(), space.getId(), false);

    //     return spaceRepository.save(space);
    // }

    // @Override
    // public Space removeMember(Long spaceId, Long nodeId) {
    //     Space space = spaceRepository.findById(spaceId)
    //         .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
    //     Node node = nodeRepository.findById(nodeId)
    //         .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
    //     if (space.getCreator().equals(node)) {
    //         throw new IllegalStateException("Cannot remove the creator from the space");
    //     }
        
    //     spaceMemberService.removeMember(space.getId(), node.getId());

    //     return spaceRepository.save(space);
    // }

    // @Override
    // @Transactional(readOnly = true)
    // public boolean isMember(Long spaceId, Long nodeId) {
    //     return spaceRepository.isMember(spaceId, nodeId);
    // }

    // @Override
    // @Transactional(readOnly = true)
    // public List<Node> getMembers(Long spaceId) {
    //     Space space = spaceRepository.findById(spaceId)
    //         .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
    //     return spaceMemberService.getMembers(space.getId());
    // }

    @Override
    public void deleteSpace(Long id) {
        spaceRepository.deleteById(id);
    }

    @Override
    public Space updateSpace(Space space) {
        if (!spaceRepository.existsById(space.getId())) {
            throw new EntityNotFoundException("Space not found with id: " + space.getId());
        }
        return spaceRepository.save(space);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Space> getSpacesByMember(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        return spaceRepository.findSpacesByMember(node);
    }
} 