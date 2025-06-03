package com.tree.space.space_member.core.services;

import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.entities.Space;

import java.util.List;
import java.util.Optional;

public interface NodeService {
    
    Node createNode(String userId, String pseudo);
    
    Optional<Node> getNodeById(Long id);
    
    Optional<Node> getNodeByUserId(String pseudo);
    
    List<Node> getAllNodes();
    
    Node updateNode(Node node);
    
    void deleteNode(Long id);
    
    List<Space> getCreatedSpaces(Long nodeId);
    
    List<Space> getMemberSpaces(Long nodeId);
    
    boolean isUserIdAvailable(String pseudo);
} 