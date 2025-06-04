package com.tree.space.space_member.api.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceMemberDTO {
    Long id;
    Long spaceId;
    Long nodeId;
    boolean isAdmin;
}
