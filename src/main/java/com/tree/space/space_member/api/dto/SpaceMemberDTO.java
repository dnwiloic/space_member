package com.tree.space.space_member.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceMemberDTO {
    private Long id;
    private SpaceDTO space;
    private NodeDTO node;
    private boolean isAdmin;
}
