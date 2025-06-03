package com.tree.space.space_member.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "discussions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discussion_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Discussion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;
    
    @OneToMany(mappedBy = "discussion", cascade = CascadeType.ALL)
    private Set<Message> messages = new HashSet<>();
} 