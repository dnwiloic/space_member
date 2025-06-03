package com.tree.space.space_member.core.services;

import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.entities.Space;

import java.util.List;
import java.util.Optional;

public interface SpaceService {
    
    Space createSpace(Long creatorId);
    
    Optional<Space> getSpaceById(Long id);

    public List<Space> getAllSpaces();
    
    // Space addMember(Long spaceId, Long nodeId);
    
    // Space removeMember(Long spaceId, Long nodeId);
    
    // boolean isMember(Long spaceId, Long nodeId);
    
    // List<Node> getMembers(Long spaceId);
    
    void deleteSpace(Long id);
    
    Space updateSpace(Space space);
    
    List<Space> getSpacesByMember(Long nodeId);
} 