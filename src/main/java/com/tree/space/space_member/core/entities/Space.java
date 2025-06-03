package com.tree.space.space_member.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "spaces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Space {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Node creator;
    
    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL)
    private Set<SpaceMember> members = new HashSet<>();
    
    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL)
    private Set<Discussion> discussions = new HashSet<>();
    
    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL)
    private Set<Event> events = new HashSet<>();
} 