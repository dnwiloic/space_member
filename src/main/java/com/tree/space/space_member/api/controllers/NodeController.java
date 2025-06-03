package com.tree.space.space_member.api.controllers;

import com.tree.space.space_member.api.dto.NodeDTO;
import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.services.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @PostMapping
    public ResponseEntity<NodeDTO> createNode(@RequestBody NodeDTO nodeDTO) {
        Node node = new Node();
        node.setUserId(nodeDTO.getUserId());
        node.setPseudo(nodeDTO.getPseudo());
        Node saved = nodeService.createNode(node.getUserId(), node.getPseudo());
        return new ResponseEntity<>(convertToDTO(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NodeDTO> getNode(@PathVariable Long id) {
        return nodeService.getNodeById(id)
                .map(node -> ResponseEntity.ok(convertToDTO(node)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<NodeDTO>> getAllNodes() {
        List<Node> nodes = nodeService.getAllNodes();
        List<NodeDTO> dtos = nodes.stream().map(NodeController::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NodeDTO> updateNode(@PathVariable Long id, @RequestBody NodeDTO nodeDTO) {
        Node node = new Node();
        node.setId(id);
        node.setPseudo(nodeDTO.getPseudo());
        node.setUserId(nodeDTO.getUserId());
        Node updated = nodeService.updateNode(node);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNode(@PathVariable Long id) {
        nodeService.deleteNode(id);
        return ResponseEntity.noContent().build();
    }

    public static NodeDTO convertToDTO(Node node) {
        return new NodeDTO(node.getId(), node.getUserId(), node.getPseudo());
    }
} 