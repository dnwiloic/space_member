package com.tree.space.space_member.core.services.impl;

import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.entities.Space;
import com.tree.space.space_member.core.repositories.NodeRepository;
import com.tree.space.space_member.core.repositories.SpaceRepository;
import com.tree.space.space_member.core.services.NodeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;
    private final SpaceRepository spaceRepository;

    @Override
    public Node createNode(String userId, String pseudo) {
        if (!isUserIdAvailable(userId)) {
            throw new IllegalArgumentException("UserId already exist: " + userId);
        }
        Node node = new Node();
        node.setUserId(userId);
        node.setPseudo(pseudo);
        return nodeRepository.save(node);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Node> getNodeById(Long id) {
        return nodeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Node> getNodeByUserId(String pseudo) {
        return nodeRepository.findByUserId(pseudo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Node> getAllNodes() {
        return nodeRepository.findAll();
    }

    @Override
    public Node updateNode(Node node) {
        if (!nodeRepository.existsById(node.getId())) {
            throw new EntityNotFoundException("Node not found with id: " + node.getId());
        }
        return nodeRepository.save(node);
    }

    @Override
    public void deleteNode(Long id) {
        nodeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Space> getCreatedSpaces(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        return spaceRepository.findByCreator(node);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Space> getMemberSpaces(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        return spaceRepository.findSpacesByMember(node);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserIdAvailable(String userId) {
        return !nodeRepository.existsByUserId(userId);
    }
} 