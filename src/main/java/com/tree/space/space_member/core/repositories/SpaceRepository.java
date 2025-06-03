package com.tree.space.space_member.core.repositories;

import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.entities.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {
    
    List<Space> findByCreator(Node creator);
    
    @Query("SELECT s FROM Space s JOIN s.members m WHERE m = :member")
    List<Space> findSpacesByMember(Node member);
    
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Space s JOIN s.members m WHERE s.id = :spaceId AND m.id = :nodeId")
    boolean isMember(Long spaceId, Long nodeId);
} 