package com.tree.space.space_member.core.repositories;

import com.tree.space.space_member.core.entities.Event;
import com.tree.space.space_member.core.entities.Node;
import com.tree.space.space_member.core.entities.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findBySpace(Space space);
    
    List<Event> findBySpaceAndStartDateGreaterThanEqual(Space space, LocalDateTime date);
    
    @Query("SELECT e FROM Event e WHERE e.startDate <= :endDate AND e.endDate >= :startDate")
    List<Event> findEventsInDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p = :participant")
    List<Event> findEventsByParticipant(Node participant);
} 