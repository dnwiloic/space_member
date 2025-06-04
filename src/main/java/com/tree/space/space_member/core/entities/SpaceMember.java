package com.tree.space.space_member.core.entities;

import jakarta.persistence.*; 
import lombok.AllArgsConstructor; 
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity 
@Table(name = "space_members") 
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
public class SpaceMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Space space;
    
    @ManyToOne
    @JoinColumn(name = "node_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Node node;
    
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false; // Par défaut, un membre n'est pas admin
    

    }