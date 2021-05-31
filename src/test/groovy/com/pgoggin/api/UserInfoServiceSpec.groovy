package com.pgoggin.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.pgoggin.api.client.async.ProjectMembershipClient
import com.pgoggin.api.client.async.RegisteredUsersClient
import com.pgoggin.api.client.async.UnregisteredUsersClient
import com.pgoggin.api.domain.ProjectMembership
import com.pgoggin.api.domain.UserInfo
import com.pgoggin.api.fixture.UserInfoServiceSpecFixture
import com.pgoggin.api.service.UserInfoService
import org.spockframework.util.Assert
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import java.util.concurrent.CompletableFuture

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserInfoServiceSpec extends Specification {
    UserInfoServiceSpecFixture fixtures = new UserInfoServiceSpecFixture()
    ProjectMembershipClient projectMembershipClient = Mock()
    RegisteredUsersClient registeredUsersClient = Mock()
    UnregisteredUsersClient unregisteredUsersClient = Mock()

    @Subject
    UserInfoService userInfoService

    def setup() {
        userInfoService = new UserInfoService(projectMembershipClient: projectMembershipClient, registeredUsersClient: registeredUsersClient, unregisteredUsersClient: unregisteredUsersClient)
    }

    def "get user info"() {
        given:
        UserInfo actualUserInfo1 = new UserInfo(id: "1", languageCode: "en", emailAddress: "email3@somewhere.com", registrationId: "jwsMJNOk3oM3hVM5bGcF3", registrationIdGeneratedTime: "156165026853")
        UserInfo actualUserInfo2 = new UserInfo(id: "2", languageCode: "en", emailAddress: "email3@somewhere.com", registrationId: "jwsMJNOk3oM3hVM5bGcF3", registrationIdGeneratedTime: "156165026853")
        UserInfo actualUserInfo3 = new UserInfo(id: "3", languageCode: "en", emailAddress: "email3@somewhere.com", registrationId: "jwsMJNOk3oM3hVM5bGcF3", registrationIdGeneratedTime: "156165026853")
        UserInfo actualUserInfo4 = new UserInfo(id: "4", languageCode: "en", emailAddress: "email3@somewhere.com", registrationId: "jwsMJNOk3oM3hVM5bGcF3", registrationIdGeneratedTime: "156165026853")
        List<UserInfo> unregisteredUserResponse = [actualUserInfo1, actualUserInfo2]
        List<UserInfo> registeredUserList = [actualUserInfo3, actualUserInfo4]
        ProjectMembership projectMembership1 = new ProjectMembership(id: "1", userId: "1", projectId: "1")
        ProjectMembership projectMembership2 = new ProjectMembership(id: "2", userId: "2", projectId: "2")
        ProjectMembership projectMembership3 = new ProjectMembership(id: "3", userId: "3", projectId: "3")
        ProjectMembership projectMembership4 = new ProjectMembership(id: "4", userId: "4", projectId: "4")
        List<ProjectMembership> projectMemberships = [projectMembership1, projectMembership2, projectMembership3, projectMembership4]

        when:
        List<UserInfo> result = userInfoService.getAllUserInfo()

        then:
        1 * unregisteredUsersClient.getFuture() >> _
        1 * registeredUsersClient.getFuture() >> _
        1 * projectMembershipClient.getFuture() >> _
        1 * unregisteredUsersClient.resolveFuture(_ as CompletableFuture) >> unregisteredUserResponse
        1 * registeredUsersClient.resolveFuture(_ as CompletableFuture) >> registeredUserList
        1 * projectMembershipClient.resolveFuture(_ as CompletableFuture) >> projectMemberships

        expect:
        Assert.that(result.size().equals(4))
        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(fixtures.expectedList)
        String actual = objectMapper.writeValueAsString(result)
        Assert.that(expected == actual)
    }

}
