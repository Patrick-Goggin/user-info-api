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

    Flux<UserInfo> getRegisteredUserFlux() {
        WebClient webClient = WebClient.builder().baseUrl(apiBaseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build()
        webClient.get().uri(registeredUsersUri).retrieve().bodyToFlux(UserInfo.class)
        .onErrorResume { e -> getDummyRegistered() }
    }

    Flux<UserInfo> getUnregisteredUserFlux() {
        WebClient webClient = WebClient.builder().baseUrl(apiBaseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build()
        webClient.get().uri(unregisteredUsersUri).retrieve().bodyToFlux(UserInfo.class)
        .onErrorResume { e -> getDummyUnregistered() }
    }

    Flux<ProjectMembership> getProjectMembershipsFlux() {
        WebClient webClient = WebClient.builder().baseUrl(apiBaseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build()
        makeProjectMembershipsApiCall(webClient)
    }

    Flux<ProjectMembership> makeProjectMembershipsApiCall(WebClient webClient) {
        webClient.get().uri(projectMembershipsUri).retrieve().bodyToFlux(ProjectMembership.class)
        .onErrorResume { e -> getDummyProjectMemberships() }
    }

    Flux<UserInfo> getDummyRegistered() {
        Flux.fromIterable(registeredUsers)
    }

    Flux<UserInfo> getDummyUnregistered() {
        Flux.fromIterable(unregisteredUsers)
    }

    Flux<ProjectMembership> getDummyProjectMemberships() {
        Flux.fromIterable(projectMemberships)
    }

    @PostConstruct
    void onInit() throws BeansException {
        ObjectMapper objectMapper = new ObjectMapper()
        registeredUsers = objectMapper.readValue(getFileContentsAsString('registeredusers-response.json'), List<UserInfo>.class)
        unregisteredUsers = objectMapper.readValue(getFileContentsAsString('unregisteredusers-response.json'), List<UserInfo>.class)
        projectMemberships = objectMapper.readValue(getFileContentsAsString('projectmemberships-response.json'), List<ProjectMembership>.class)
    }

    String getFileContentsAsString(String fileName) {
        String filePath = "src/main/resources/upstream-api-mock-responses/${fileName}"
        File file = new File(filePath)
        file.text
    }
}
