package com.tree.space.space_member.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String path;
    
    @Column(nullable = false)
    private String type;
    
    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;
} 