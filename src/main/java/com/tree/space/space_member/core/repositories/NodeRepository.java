package com.tree.space.space_member.core.repositories;

import com.tree.space.space_member.core.entities.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    
    Optional<Node> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
} 