package com.tree.space.space_member.core.services;

import com.tree.space.space_member.core.entities.*;

import java.util.List;
import java.util.Optional;

public interface DiscussionService {
    
    GroupDiscussion createGroupDiscussion(Long spaceId);
    
    EventDiscussion createEventDiscussion(Long spaceId, Long eventId);
    
    PeerToPeerDiscussion createP2PDiscussion(Long spaceId, List<Long> participantIds);
    
    Optional<Discussion> getDiscussionById(Long id);
    
    List<Discussion> getDiscussionsBySpace(Long spaceId);
    
    void deleteDiscussion(Long id);
    
    List<Message> getDiscussionMessages(Long discussionId);
    
    long getMessageCount(Long discussionId);
    
    // Méthodes spécifiques pour P2P
    List<Node> getP2PParticipants(Long discussionId);
    
    void addP2PParticipant(Long discussionId, Long nodeId);
    
    void removeP2PParticipant(Long discussionId, Long nodeId);
} 