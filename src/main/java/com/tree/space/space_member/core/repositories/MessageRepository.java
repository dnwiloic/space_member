package com.tree.space.space_member.core.repositories;

import com.tree.space.space_member.core.entities.Discussion;
import com.tree.space.space_member.core.entities.Message;
import com.tree.space.space_member.core.entities.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    Page<Message> findByDiscussionOrderBySendDateDesc(Discussion discussion, Pageable pageable);
    
    List<Message> findBySender(Node sender);
    
    List<Message> findByReplyTo(Message replyTo);
    
    long countByDiscussion(Discussion discussion);
} 