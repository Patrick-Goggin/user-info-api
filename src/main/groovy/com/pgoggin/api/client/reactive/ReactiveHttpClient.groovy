package com.pgoggin.api.client.reactive

import com.fasterxml.jackson.databind.ObjectMapper
import com.pgoggin.api.domain.ProjectMembership
import com.pgoggin.api.domain.UserInfo
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

import javax.annotation.PostConstruct

@Component
class ReactiveHttpClient {
    @Value('${api.base.url}')
    String apiBaseUrl
    @Value('${api.project.memberships.uri}')
    String projectMembershipsUri
    @Value('${api.registered.users.uri}')
    String registeredUsersUri
    @Value('${api.unregistered.users.uri}')
    String unregisteredUsersUri

    List<UserInfo> unregisteredUsers
    List<UserInfo> registeredUsers
    List<ProjectMembership> projectMemberships

    Flux<UserInfo> getUserInfo() {
        WebClient webClient = WebClient.builder().baseUrl(apiBaseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build()
        Flux<UserInfo> registeredUsers = getRegisteredUsers(webClient).onErrorResume { e -> getDummyRegistered() }
        Flux<UserInfo> unregisteredUsers = getUnregisteredUsers(webClient) onErrorResume { e -> getDummyUnregistered() }
        Flux<UserInfo> result = Flux.concat(registeredUsers, unregisteredUsers)
        return result.onErrorResume { e -> getDummyUserInfo() }
    }

    Flux<ProjectMembership> getProjectMemberships() {
        WebClient webClient = WebClient.builder().baseUrl(apiBaseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build()
        return makeProjectMembershipsApiCall(webClient).onErrorResume { e -> getDummyProjectMemberships() }
    }

    Flux<ProjectMembership> makeProjectMembershipsApiCall(WebClient webClient) {
        webClient.get().uri(projectMembershipsUri).retrieve().bodyToFlux(ProjectMembership.class).onErrorResume { e -> getDummyProjectMemberships() }
    }

    Flux<UserInfo> getRegisteredUsers(WebClient webClient) {
        webClient.get().uri(registeredUsersUri).retrieve().bodyToFlux(UserInfo.class).onErrorResume { e -> getDummyRegistered() }
    }

    Flux<UserInfo> getUnregisteredUsers(WebClient webClient) {
        webClient.get().uri(unregisteredUsersUri).retrieve().bodyToFlux(UserInfo.class).onErrorResume { e -> getDummyUnregistered() }
    }

    Flux<UserInfo> getDummyUserInfo() {
        Flux<UserInfo> registeredUsers = Flux.fromIterable(registeredUsers)
        Flux<UserInfo> unregisteredUsers = Flux.fromIterable(unregisteredUsers)
        Flux<UserInfo> userInfo = Flux.concat(registeredUsers, unregisteredUsers)
        userInfo
    }

    Flux<UserInfo> getDummyRegistered() {
        return Flux.fromIterable(registeredUsers)
    }

    Flux<UserInfo> getDummyUnregistered() {
        return Flux.fromIterable(unregisteredUsers)
    }

    Flux<ProjectMembership> getDummyProjectMemberships() {
        Flux.fromIterable(projectMemberships)
    }

    String getFileContentsAsString(String fileName) {
        String filePath = "src/main/resources/upstream-api-mock-responses/${fileName}"
        File file = new File(filePath)
        file.text
    }

    @PostConstruct
    void onInit() throws BeansException {
        ObjectMapper objectMapper = new ObjectMapper()
        this.registeredUsers = objectMapper.readValue(getFileContentsAsString('registeredusers-response.json'), List<UserInfo>.class)
        this.unregisteredUsers = objectMapper.readValue(getFileContentsAsString('unregisteredusers-response.json'), List<UserInfo>.class)
        this.projectMemberships = objectMapper.readValue(getFileContentsAsString('projectmemberships-response.json'), List<ProjectMembership>.class)
    }
}
