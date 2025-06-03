package com.tree.space.space_member.core.repositories;

import com.tree.space.space_member.core.entities.Media;
import com.tree.space.space_member.core.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
    List<Media> findByMessage(Message message);
    
    List<Media> findByType(String type);
    
    void deleteByMessage(Message message);
} 