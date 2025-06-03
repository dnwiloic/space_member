package com.tree.space.space_member.core.repositories;

import com.tree.space.space_member.core.entities.Discussion;
import com.tree.space.space_member.core.entities.Message;
import com.tree.space.space_member.core.entities.MessageReadStatus;
import com.tree.space.space_member.core.entities.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus, Long> {
    
    Optional<MessageReadStatus> findByMessageAndReader(Message message, Node reader);
    
    List<MessageReadStatus> findByMessage(Message message);
    
    List<MessageReadStatus> findByReader(Node reader);
    
    @Query("SELECT mrs FROM MessageReadStatus mrs WHERE mrs.message.discussion = :discussion AND mrs.reader = :reader")
    List<MessageReadStatus> findByDiscussionAndReader(Discussion discussion, Node reader);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.discussion = :discussion AND NOT EXISTS " +
           "(SELECT mrs FROM MessageReadStatus mrs WHERE mrs.message = m AND mrs.reader = :reader AND mrs.read = true)")
    long countUnreadMessagesInDiscussion(Discussion discussion, Node reader);
} 