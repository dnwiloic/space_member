package com.tree.space.space_member.core.repositories;

import com.tree.space.space_member.core.entities.Discussion;
import com.tree.space.space_member.core.entities.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    
    List<Discussion> findBySpace(Space space);
    
    List<Discussion> findBySpaceOrderByIdDesc(Space space);
} 