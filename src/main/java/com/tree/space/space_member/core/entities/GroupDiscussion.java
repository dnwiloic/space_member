package com.tree.space.space_member.core.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("GROUP")
@NoArgsConstructor
public class GroupDiscussion extends Discussion {
    // Attributs spécifiques aux discussions de groupe si nécessaire
} 