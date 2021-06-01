package com.pgoggin.api.service

import com.pgoggin.api.client.reactive.ReactiveHttpClient
import com.pgoggin.api.domain.ProjectMembership
import com.pgoggin.api.domain.UserInfo
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Log
@Service
class ReactiveUserInfoService {
    @Autowired
    ReactiveHttpClient reactiveClient

    Mono<List<UserInfo>> getUserInfo() {
//        Flux<UserInfo> registeredUsersFlux = reactiveClient.getRegisteredUserFlux()
//        Flux<UserInfo> unregisteredUsersFlux = reactiveClient.getUnregisteredUserFlux()
//        Flux<ProjectMembership> projectMembershipFlux = reactiveClient.getProjectMembershipsFlux()
        Flux<UserInfo> registeredUsersFlux = reactiveClient.getDummyRegistered()
        Flux<UserInfo> unregisteredUsersFlux = reactiveClient.getDummyUnregistered()
        Flux<ProjectMembership> projectMembershipFlux = reactiveClient.getDummyProjectMemberships()
        Flux<UserInfo> userInfoFlux = Flux.concat(registeredUsersFlux, unregisteredUsersFlux)
        Mono<List<UserInfo>> result = getUsersAndProjectMemberships(userInfoFlux, projectMembershipFlux)
        result as Mono<List<UserInfo>>
    }

    Mono<List<UserInfo>> getUsersAndProjectMemberships(Flux<UserInfo> userInfoFlux, Flux<ProjectMembership> projectMembershipFlux) {
        Mono<List<UserInfo>> userInfoListMono = getUserInfoListMono(userInfoFlux)
        Mono<Map<String, List<String>>> projectMembershipsMapMono = getProjectIdsMapMono(projectMembershipFlux)
        Mono<List<UserInfo>> result = userInfoListMono.zipWith(projectMembershipsMapMono, {
            userInfos, projectMembershipsMap ->
                userInfos.each { userInfo ->
                    List<String> projectIds = projectMembershipsMap[userInfo.id]
                    userInfo.projectIds = (projectIds) ? projectIds.sort { it -> Integer.parseInt(it) } : []
                }
                userInfos.sort { userInfo -> Integer.parseInt(userInfo.id) }
        })
        result
    }

    private Mono<Map<String, List<String>>> getProjectIdsMapMono(Flux<ProjectMembership> projectMembershipFlux) {
        Map<String, List<String>> userIdProjectMembershipMap = new HashMap<>()
        projectMembershipFlux.subscribe { projectMembership ->
            if (userIdProjectMembershipMap[projectMembership.userId]) {
                userIdProjectMembershipMap[projectMembership.userId] << projectMembership.projectId
            } else {
                userIdProjectMembershipMap.put(projectMembership.userId, [projectMembership.projectId])
            }
        }
        Mono.just(userIdProjectMembershipMap)
    }

    private getUserInfoListMono(Flux<UserInfo> userInfoFlux) {
        List<UserInfo> userInfoList = new ArrayList<>()
        userInfoFlux.subscribe { userInfo -> userInfoList << userInfo }
        Mono.just(userInfoList)
    }
}
