package com.tree.space.space_member.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Column(nullable = false)
    private LocalDateTime sendDate;
    
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Node sender;
    
    @ManyToOne
    @JoinColumn(name = "discussion_id", nullable = false)
    private Discussion discussion;
    
    @ManyToOne
    @JoinColumn(name = "reply_to_id")
    private Message replyTo;
    
    @OneToMany(mappedBy = "replyTo")
    private Set<Message> replies = new HashSet<>();
    
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private Set<Media> medias = new HashSet<>();
} 