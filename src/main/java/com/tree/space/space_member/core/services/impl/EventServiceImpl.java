package com.tree.space.space_member.core.services.impl;

import com.tree.space.space_member.core.entities.Event;
import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.entities.Space;
import com.tree.space.space_member.core.repositories.EventRepository;
import com.tree.space.space_member.core.repositories.NodeRepository;
import com.tree.space.space_member.core.repositories.SpaceRepository;
import com.tree.space.space_member.core.services.EventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final SpaceRepository spaceRepository;
    private final NodeRepository nodeRepository;

    @Override
    public Event createEvent(Long spaceId, String title, String description,
                           LocalDateTime startDate, LocalDateTime endDate) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
        
        Event event = new Event();
        event.setSpace(space);
        event.setTitle(title);
        event.setDescription(description);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        
        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsBySpace(Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
        return eventRepository.findBySpace(space);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getUpcomingEvents(Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
        return eventRepository.findBySpaceAndStartDateGreaterThanEqual(space, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findEventsInDateRange(startDate, endDate);
    }

    @Override
    public Event addParticipant(Long eventId, Long nodeId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
        Node node = nodeRepository.findById(nodeId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        
        event.getParticipants().add(node);
        return eventRepository.save(event);
    }

    @Override
    public Event removeParticipant(Long eventId, Long nodeId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
        Node node = nodeRepository.findById(nodeId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        
        event.getParticipants().remove(node);
        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Node> getParticipants(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
        return new ArrayList<>(event.getParticipants());
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event updateEvent(Event event) {
        if (!eventRepository.existsById(event.getId())) {
            throw new EntityNotFoundException("Event not found with id: " + event.getId());
        }
        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByParticipant(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        return eventRepository.findEventsByParticipant(node);
    }
} 