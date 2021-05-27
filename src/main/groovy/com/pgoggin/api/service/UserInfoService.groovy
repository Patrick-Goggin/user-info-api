package com.pgoggin.api.service

import com.pgoggin.api.client.ProjectMembershipClient
import com.pgoggin.api.client.RegisteredUsersClient
import com.pgoggin.api.client.UnregisteredUsersClient
import com.pgoggin.api.domain.ProjectMembership
import com.pgoggin.api.domain.UserInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils

import java.util.concurrent.CompletableFuture

@Service("userInfoService")
class UserInfoService {
    @Autowired
    ProjectMembershipClient projectMembershipClient
    @Autowired
    RegisteredUsersClient registeredUsersClient
    @Autowired
    UnregisteredUsersClient unregisteredUsersClient

    List<UserInfo> getAllUserInfo() {
        CompletableFuture registeredUsersFuture = registeredUsersClient.getFuture()
        CompletableFuture unregisteredUsersFuture = unregisteredUsersClient.getFuture()
        CompletableFuture projectMemberShipsFuture = projectMembershipClient.getFuture()
        buildUserInfoList(registeredUsersFuture, unregisteredUsersFuture, projectMemberShipsFuture)
    }

    private List<UserInfo> buildUserInfoList(CompletableFuture registeredUsersFuture, CompletableFuture unregisteredUsersFuture, CompletableFuture projectMemberShipsFuture) {
        Map<String, UserInfo> userInfoMap = buildUserInfoMap(registeredUsersFuture, unregisteredUsersFuture, projectMemberShipsFuture)
        if (CollectionUtils.isEmpty(userInfoMap)) {
            return new ArrayList<>()
        }
        List<UserInfo> userInfoList = new ArrayList<>(userInfoMap.values())
        userInfoList.sort { a, b ->
            Integer.parseInt(a.id) <=> Integer.parseInt(b.id)
        }
        userInfoList
    }

    private Map<String, UserInfo> buildUserInfoMap(CompletableFuture registeredUsersFuture, CompletableFuture unregisteredUsersFuture, CompletableFuture projectMemberShipsFuture) {
        Map<String, UserInfo> userInfoMap = new HashMap<>()
        List<UserInfo> registeredUsers = registeredUsersClient.resolveFuture(registeredUsersFuture) as List<UserInfo>
        List<UserInfo> unregisteredUsers = unregisteredUsersClient.resolveFuture(unregisteredUsersFuture) as List<UserInfo>
        registeredUsers.addAll(unregisteredUsers)
        registeredUsers.each { it ->
            UserInfo userInfo = it as UserInfo
            userInfoMap.put(userInfo.getId(), userInfo)
        }
        addProjectMemberships(projectMemberShipsFuture, userInfoMap)
        userInfoMap
    }

    private void addProjectMemberships(CompletableFuture projectMemberShipsFuture, Map<String, UserInfo> userInfoMap) {
        List<ProjectMembership> projectMemberships = projectMembershipClient.resolveFuture(projectMemberShipsFuture) as List<ProjectMembership>
        projectMemberships.each { it ->
            ProjectMembership projectMembership = it as ProjectMembership
            findUserAndAddProjectMembership(projectMembership, userInfoMap)
        }
    }

    private void findUserAndAddProjectMembership(ProjectMembership projectMembership, Map<String, UserInfo> userInfoMap) {
        UserInfo userInfo = userInfoMap.get(projectMembership.getUserId())
        if (userInfo != null) {
            userInfo.addProjectId(projectMembership.getProjectId())
            userInfo.getProjectIds().sort { a, b ->
                Integer.parseInt(a) <=> Integer.parseInt(b)
            }
        }
    }
}
