package com.tree.space.space_member.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceDTO {
    private Long id;
    private Long creatorId;
    private Set<Long> spaceMemberIds;
} 