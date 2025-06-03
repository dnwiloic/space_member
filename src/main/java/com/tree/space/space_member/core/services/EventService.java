package com.tree.space.space_member.core.services;

import com.tree.space.space_member.core.entities.Event;
import com.tree.space.space_member.core.entities.Node;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventService {
    
    Event createEvent(Long spaceId, String title, String description, 
                     LocalDateTime startDate, LocalDateTime endDate);
    
    Optional<Event> getEventById(Long id);
    
    List<Event> getEventsBySpace(Long spaceId);
    
    List<Event> getUpcomingEvents(Long spaceId);
    
    List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    Event addParticipant(Long eventId, Long nodeId);
    
    Event removeParticipant(Long eventId, Long nodeId);
    
    List<Node> getParticipants(Long eventId);
    
    void deleteEvent(Long id);
    
    Event updateEvent(Event event);
    
    List<Event> getEventsByParticipant(Long nodeId);
} 