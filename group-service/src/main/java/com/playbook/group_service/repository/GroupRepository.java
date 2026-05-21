package com.playbook.group_service.repository;


import com.playbook.group_service.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface GroupRepository extends MongoRepository<Group, String> {
    Optional<Group> findByInviteCode(String inviteCode);
    List<Group> findByMemberUserIdsContaining(String userId);
}