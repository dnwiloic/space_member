package com.tree.space.space_member.core.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("P2P")
@Data
@NoArgsConstructor
public class PeerToPeerDiscussion extends Discussion {
    
    @ManyToMany
    @JoinTable(
        name = "p2p_discussion_participants",
        joinColumns = @JoinColumn(name = "discussion_id"),
        inverseJoinColumns = @JoinColumn(name = "node_id")
    )
    private Set<Node> participants = new HashSet<>();
} 