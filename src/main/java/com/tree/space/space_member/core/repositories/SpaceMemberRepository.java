package com.tree.space.space_member.core.repositories;

import com.tree.space.space_member.core.entities.SpaceMember; 
import java.util.List; 
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceMemberRepository extends JpaRepository<SpaceMember, Long> { 

    List<SpaceMember> findBySpaceId(Long spaceId); 
    List<SpaceMember> findByNodeId(Long nodeId); 


    @Query("SELECT sm FROM SpaceMember sm WHERE sm.space.id = :spaceId AND sm.node.id = :nodeId")
    Optional<SpaceMember> findBySpaceIdAndNodeId(Long spaceId, Long nodeId);

    @Query("SELECT CASE WHEN COUNT(sm) > 0 THEN true ELSE false END FROM SpaceMember sm WHERE sm.space.id = :spaceId AND sm.node.id = :nodeId")
    boolean isMember(Long spaceId, Long nodeId);

    @Query("SELECT CASE WHEN COUNT(sm) > 0 THEN true ELSE false END FROM SpaceMember sm WHERE sm.space.id = :spaceId AND sm.node.id = :nodeId AND sm.isAdmin = true")
    boolean isAdmin(Long spaceId, Long nodeId);
}