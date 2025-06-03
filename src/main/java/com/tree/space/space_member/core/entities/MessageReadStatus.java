package com.tree.space.space_member.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "message_read_status",
    uniqueConstraints = @UniqueConstraint(columnNames = {"message_id", "reader_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageReadStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;
    
    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private Node reader;
    
    @Column(nullable = false)
    private boolean read;
    
    @Column(name = "read_date")
    private LocalDateTime readDate;
} 