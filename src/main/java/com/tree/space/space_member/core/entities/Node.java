package com.tree.space.space_member.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "nodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String pseudo;
    
    // Relation avec les espaces créés par ce node
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private Set<Space> createdSpaces = new HashSet<>();
    
    // Relation one-to-many avec les SpaceMember 
    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL)
    private Set<SpaceMember> memberships = new HashSet<>();
} 