package com.tree.space.space_member.core.services;

import com.tree.space.space_member.core.entities.Node; 
import com.tree.space.space_member.core.entities.Space; 
import com.tree.space.space_member.core.entities.SpaceMember; 
import java.util.List;
import java.util.Optional;

public interface SpaceMemberService { 
    public Optional<SpaceMember> getSpaceMemberById(Long id);
    SpaceMember addMember(Long spaceId, Long nodeId, boolean isAdmin); 
    void removeMember(Long spaceId, Long nodeId); 
    List<SpaceMember> getMembers(Long spaceId); 
    List<SpaceMember> getAdmins(Long spaceId); 
    void updateAdminStatus(Long spaceId, Long nodeId, boolean isAdmin); 
}