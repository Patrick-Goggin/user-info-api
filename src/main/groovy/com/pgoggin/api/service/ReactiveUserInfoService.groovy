package com.pgoggin.api.service


import com.pgoggin.api.client.reactive.ReactiveHttpClient
import com.pgoggin.api.domain.ProjectMembership
import com.pgoggin.api.domain.UserInfo
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Log
@Service
class ReactiveUserInfoService {
    @Autowired
    ReactiveHttpClient reactiveClient

    Mono<List<UserInfo>> getUserInfo() {
        Flux<UserInfo> userInfoFlux = reactiveClient.getUserInfo().onErrorResume { e -> userInfoFlux = reactiveClient.getDummyUserInfo() }
        Flux<ProjectMembership> projectMembershipFlux = reactiveClient.getProjectMemberships().onErrorResume { e -> projectMembershipFlux = reactiveClient.getDummyProjectMemberships() }
        Mono<List<UserInfo>> result = getUsersAndProjectMemberships(userInfoFlux, projectMembershipFlux)
        result as Mono<List<UserInfo>>
    }

    Mono<Map<String, UserInfo>> getUserIdAndUserInfoMapAsMono(Flux<UserInfo> userInfoFlux, Flux<ProjectMembership> projectMembershipFlux) {
        Mono<Map<String, UserInfo>> idsAndUsersMapMono = userInfoFlux.collectMap(
            { userInfo -> userInfo.id },
            { userInfo ->
                userInfo.projectIds = []
                Mono<List<ProjectMembership>> projectMembershipMono = getProjectMembershipsMonoByUserId(userInfo.id, projectMembershipFlux as Flux<ProjectMembership>)
                projectMembershipMono.map { it ->
                    it.collect { p -> p.projectId }
                }.subscribe {
                    it -> userInfo.projectIds.addAll(it)
                }
                userInfo
            }
        ) as Mono<Map<String, UserInfo>>
        idsAndUsersMapMono
    }

    Mono<List<UserInfo>> getUsersAndProjectMemberships(Flux<UserInfo> userInfoFlux, Flux<ProjectMembership> projectMembershipFlux) {
        Mono<Map<String, UserInfo>> userMapMono = getUserIdAndUserInfoMapAsMono(userInfoFlux, projectMembershipFlux)
        Mono<List<ProjectMembership>> projectMembershipsMono = projectMembershipFlux.collectList()
        List<ProjectMembership> projectMemberships = projectMembershipsMono.block()
        Map<String, UserInfo> userMap = userMapMono.block()
        projectMemberships.each { projectMembership ->
            UserInfo userInfo = userMap[projectMembership.userId]
            if (userInfo) {
                userInfo.projectIds << projectMembership.projectId
            }
        }
        List<UserInfo> userInfoList = new ArrayList<>(userMap.values().sort { it.id })
        Mono.just(userInfoList.sort { userInfo -> Integer.parseInt(userInfo.id) })
    }

    private Mono<List<ProjectMembership>> getProjectMembershipsMonoByUserId(String userId, Flux<ProjectMembership> projectMemberships) {
        List<ProjectMembership> projectMembershipList = new ArrayList<>()
        projectMemberships.subscribe { projectMembership ->
            if (projectMembership.userId == userId) {
                projectMembershipList.add(projectMembership)
            }
        }
        return Mono.just(projectMembershipList)
    }
}
