package com.playbook.group_service.controller;


import com.playbook.group_service.dto.AssignBookRequest;
import com.playbook.group_service.dto.CreateGroupRequest;
import com.playbook.group_service.entity.Group;
import com.playbook.group_service.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestHeader("X-User-Id") String userId,
                                             @RequestBody CreateGroupRequest request) {
        return ResponseEntity.ok(groupService.createGroup(request, userId));
    }

    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<Group> joinGroup(@PathVariable String inviteCode,
                                           @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(groupService.joinGroup(inviteCode, userId));
    }

    @PostMapping("/{groupId}/leave")
    public ResponseEntity<Group> leaveGroup(@PathVariable String groupId,
                                            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(groupService.leaveGroup(groupId, userId));
    }

    @DeleteMapping("/{groupId}/members/{targetUserId}")
    public ResponseEntity<Group> removeMember(@PathVariable String groupId,
                                              @RequestHeader("X-User-Id") String adminUserId,
                                              @PathVariable String targetUserId) {
        return ResponseEntity.ok(groupService.removeMember(groupId, adminUserId, targetUserId));
    }

    @PostMapping("/{groupId}/assign-book")
    public ResponseEntity<Group> assignBook(@PathVariable String groupId,
                                            @RequestHeader("X-User-Id") String adminUserId,
                                            @RequestBody AssignBookRequest request) {
        return ResponseEntity.ok(groupService.assignBook(groupId, adminUserId, request));
    }

    @DeleteMapping("/{groupId}/assigned-book")
    public ResponseEntity<Group> removeAssignedBook(@PathVariable String groupId,
                                                    @RequestHeader("X-User-Id") String adminUserId) {
        return ResponseEntity.ok(groupService.removeAssignedBook(groupId, adminUserId));
    }

    @PostMapping("/{groupId}/complete-deadline")
    public ResponseEntity<Group> completeDeadline(@PathVariable String groupId,
                                                  @RequestHeader("X-User-Id") String adminUserId) {
        return ResponseEntity.ok(groupService.completeDeadline(groupId, adminUserId));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String groupId,
                                            @RequestHeader("X-User-Id") String adminUserId) {
        groupService.deleteGroup(groupId, adminUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Group> getGroup(@PathVariable String groupId,
                                          @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(groupService.getGroup(groupId, userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Group>> getMyGroups(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(groupService.getUserGroups(userId));
    }
}