package com.tree.space.space_member.core.entities;

import jakarta.persistence.*; 
import lombok.AllArgsConstructor; 
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "space_members") 
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class SpaceMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;
    
    @ManyToOne
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;
    
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false; // Par défaut, un membre n'est pas admin
    

    // public boolean getIsAdmin(){
    //     return this.isAdmin;
    // }
    }