package com.tree.space.space_member.core.services.impl;

import com.tree.space.space_member.api.exceptions.MemberAlreadyExistsException;
import com.tree.space.space_member.core.entities.Node; 
import com.tree.space.space_member.core.entities.Space; 
import com.tree.space.space_member.core.entities.SpaceMember; 
import com.tree.space.space_member.core.repositories.NodeRepository; 
import com.tree.space.space_member.core.repositories.SpaceMemberRepository; 
import com.tree.space.space_member.core.repositories.SpaceRepository; 
import com.tree.space.space_member.core.services.SpaceMemberService; 
import jakarta.persistence.EntityNotFoundException; 
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service 
@Transactional
@RequiredArgsConstructor
public class SpaceMemberServiceImpl implements SpaceMemberService {

    private final SpaceMemberRepository spaceMemberRepository;
    private final SpaceRepository spaceRepository;
    private final NodeRepository nodeRepository;
    private static final Logger logger = LoggerFactory.getLogger(SpaceMemberServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public Optional<SpaceMember> getSpaceMemberById(Long id) {
        return spaceMemberRepository.findById(id);
    }

    @Override
    @Transactional
    public SpaceMember addMember(Long spaceId, Long nodeId, boolean isAdmin) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new EntityNotFoundException("Space not found with id: " + spaceId));
        Node node = nodeRepository.findById(nodeId)
            .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));

        // Vérifier si le membre existe déjà
        logger.info("nodeId {} and spaceId {} exist", node.getId(), space.getId());
        if (spaceMemberRepository.isMember(space.getId(), node.getId())) {
            logger.info("nodeId {} and spaceId {} already linked", node.getId(), space.getId());
            throw new MemberAlreadyExistsException("Node with id " + nodeId + " is already a member of space " + spaceId);
        }

        logger.info("nodeId {} and spaceId {} not already linked", node.getId(), space.getId());


        SpaceMember spaceMember = new SpaceMember();
        spaceMember.setSpace(space);
        spaceMember.setNode(node);
        spaceMember.setAdmin(isAdmin);
        spaceMember = spaceMemberRepository.save(spaceMember);
        logger.info("space member {} created", spaceMember.getId() );

        space.getMembers().add(spaceMember);
        logger.info("adding spaceMember {} to space  {}", spaceMember.getId(), space.getId() );

        node.getMemberships().add(spaceMember);
        logger.info("adding spaceMember {} to node  {}", spaceMember.getId(), node.getId() );
        
        nodeRepository.save(node);
        logger.info("node saved" );
        spaceRepository.save(space);
        logger.info("space saved" );
        

        return spaceMember;
    }

    @Override
    @Transactional
    public void removeMember(Long spaceId, Long nodeId) {
        SpaceMember spaceMember = spaceMemberRepository.findBySpaceId(spaceId).stream()
            .filter(sm -> sm.getNode().getId().equals(nodeId))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Member with node id " + nodeId + " not found in space " + spaceId));

        spaceMember.getSpace().getMembers().remove(spaceMember);
        spaceMember.getNode().getMemberships().remove(spaceMember);
        spaceMemberRepository.delete(spaceMember);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpaceMember> getMembers(Long spaceId) {
        return spaceMemberRepository.findBySpaceId(spaceId).stream()
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpaceMember> getAdmins(Long spaceId) {
        return spaceMemberRepository.findBySpaceId(spaceId).stream()
            .filter(SpaceMember::isAdmin)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateAdminStatus(Long spaceId, Long nodeId, boolean isAdmin) {
        SpaceMember spaceMember = spaceMemberRepository.findBySpaceId(spaceId).stream()
            .filter(sm -> sm.getNode().getId().equals(nodeId))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Member with node id " + nodeId + " not found in space " + spaceId));

        spaceMember.setAdmin(isAdmin);
        spaceMemberRepository.save(spaceMember);
    }

}