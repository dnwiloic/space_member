package com.tree.space.space_member.core.services.impl;

import com.tree.space.space_member.core.entities.*;
import com.tree.space.space_member.core.repositories.DiscussionRepository;
import com.tree.space.space_member.core.repositories.EventRepository;
import com.tree.space.space_member.core.repositories.MessageRepository;
import com.tree.space.space_member.core.repositories.NodeRepository;
import com.tree.space.space_member.core.repositories.SpaceRepository;
import com.tree.space.space_member.core.services.DiscussionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final SpaceRepository spaceRepository;
    private final NodeRepository nodeRepository;
    private final EventRepository eventRepository;
    private final MessageRepository messageRepository;

    @Override
    public GroupDiscussion createGroupDiscussion(Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
        
        GroupDiscussion discussion = new GroupDiscussion();
        discussion.setSpace(space);
        return discussionRepository.save(discussion);
    }

    @Override
    public EventDiscussion createEventDiscussion(Long spaceId, Long eventId) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
        
        EventDiscussion discussion = new EventDiscussion();
        discussion.setSpace(space);
        discussion.setEvent(event);
        return discussionRepository.save(discussion);
    }

    @Override
    public PeerToPeerDiscussion createP2PDiscussion(Long spaceId, List<Long> participantIds) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
        
        List<Node> participants = participantIds.stream()
            .map(id -> nodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + id)))
            .collect(Collectors.toList());
        
        PeerToPeerDiscussion discussion = new PeerToPeerDiscussion();
        discussion.setSpace(space);
        discussion.getParticipants().addAll(participants);
        return discussionRepository.save(discussion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Discussion> getDiscussionById(Long id) {
        return discussionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Discussion> getDiscussionsBySpace(Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
        return discussionRepository.findBySpace(space);
    }

    @Override
    public void deleteDiscussion(Long id) {
        discussionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getDiscussionMessages(Long discussionId) {
        Discussion discussion = discussionRepository.findById(discussionId)
            .orElseThrow(() -> new EntityNotFoundException("Discussion not found with id: " + discussionId));
        return messageRepository.findByDiscussionOrderBySendDateDesc(discussion, null).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public long getMessageCount(Long discussionId) {
        Discussion discussion = discussionRepository.findById(discussionId)
            .orElseThrow(() -> new EntityNotFoundException("Discussion not found with id: " + discussionId));
        return messageRepository.countByDiscussion(discussion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Node> getP2PParticipants(Long discussionId) {
        Discussion discussion = discussionRepository.findById(discussionId)
            .orElseThrow(() -> new EntityNotFoundException("Discussion not found with id: " + discussionId));
        
        if (!(discussion instanceof PeerToPeerDiscussion)) {
            throw new IllegalArgumentException("Discussion is not a P2P discussion");
        }
        
        return new ArrayList<>(((PeerToPeerDiscussion) discussion).getParticipants());
    }

    @Override
    public void addP2PParticipant(Long discussionId, Long nodeId) {
        Discussion discussion = discussionRepository.findById(discussionId)
            .orElseThrow(() -> new EntityNotFoundException("Discussion not found with id: " + discussionId));
        
        if (!(discussion instanceof PeerToPeerDiscussion)) {
            throw new IllegalArgumentException("Discussion is not a P2P discussion");
        }
        
        Node node = nodeRepository.findById(nodeId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        
        ((PeerToPeerDiscussion) discussion).getParticipants().add(node);
        discussionRepository.save(discussion);
    }

    @Override
    public void removeP2PParticipant(Long discussionId, Long nodeId) {
        Discussion discussion = discussionRepository.findById(discussionId)
            .orElseThrow(() -> new EntityNotFoundException("Discussion not found with id: " + discussionId));
        
        if (!(discussion instanceof PeerToPeerDiscussion)) {
            throw new IllegalArgumentException("Discussion is not a P2P discussion");
        }
        
        Node node = nodeRepository.findById(nodeId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        
        ((PeerToPeerDiscussion) discussion).getParticipants().remove(node);
        discussionRepository.save(discussion);
    }
} 