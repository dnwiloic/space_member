package com.tree.space.space_member.core.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("EVENT")
@Data
@NoArgsConstructor
public class EventDiscussion extends Discussion {
    
    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;
} 