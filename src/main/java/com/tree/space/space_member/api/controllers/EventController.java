package com.tree.space.space_member.api.controllers;

import com.tree.space.space_member.api.dto.EventDTO;
import com.tree.space.space_member.core.entities.Event;
import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.services.EventService;
import com.tree.space.space_member.core.services.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final NodeService nodeService;

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        Event event = new Event();
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setStartDate(eventDTO.getStartDate());
        event.setEndDate(eventDTO.getEndDate());
        // L'association à l'espace et aux participants se fait dans le service
        Event saved = eventService.createEvent(eventDTO.getSpaceId(), eventDTO.getTitle(), eventDTO.getDescription(), eventDTO.getStartDate(), eventDTO.getEndDate());
        return new ResponseEntity<>(convertToDTO(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(event -> ResponseEntity.ok(convertToDTO(event)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/space/{spaceId}")
    public ResponseEntity<List<EventDTO>> getEventsBySpace(@PathVariable Long spaceId) {
        List<Event> events = eventService.getEventsBySpace(spaceId);
        List<EventDTO> dtos = events.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        Event event = new Event();
        event.setId(id);
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setStartDate(eventDTO.getStartDate());
        event.setEndDate(eventDTO.getEndDate());
        // L'association à l'espace et aux participants se fait dans le service
        Event updated = eventService.updateEvent(event);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{eventId}/participants/{nodeId}")
    public ResponseEntity<EventDTO> addParticipant(@PathVariable Long eventId, @PathVariable Long nodeId) {
        Event event = eventService.addParticipant(eventId, nodeId);
        return ResponseEntity.ok(convertToDTO(event));
    }

    @DeleteMapping("/{eventId}/participants/{nodeId}")
    public ResponseEntity<EventDTO> removeParticipant(@PathVariable Long eventId, @PathVariable Long nodeId) {
        Event event = eventService.removeParticipant(eventId, nodeId);
        return ResponseEntity.ok(convertToDTO(event));
    }

    private EventDTO convertToDTO(Event event) {
        Long spaceId = event.getSpace() != null ? event.getSpace().getId() : null;
        Set<Long> participantsIds = event.getParticipants() != null ? event.getParticipants().stream().map(Node::getId).collect(Collectors.toSet()) : new HashSet<>();
        return new EventDTO(event.getId(), event.getTitle(), event.getDescription(), event.getStartDate(), event.getEndDate(), spaceId, participantsIds);
    }
} 