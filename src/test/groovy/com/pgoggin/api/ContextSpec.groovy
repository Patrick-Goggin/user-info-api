package com.pgoggin.api

import org.spockframework.util.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application)
class ContextSpec extends Specification {
    @Autowired
    ApplicationContext context

    def "context is as expected"() {
        expect:
        context
        Object userInfoServiceBean = context.getBean("userInfoService")
        Object registeredUsersClientBean = context.getBean("registeredUsersClient")
        Object unregisteredUsersClientBean = context.getBean("unregisteredUsersClient")
        Object projectMembershipClientBean = context.getBean("projectMembershipClient")
        Assert.notNull(userInfoServiceBean);
        Assert.notNull(registeredUsersClientBean);
        Assert.notNull(unregisteredUsersClientBean);
        Assert.notNull(projectMembershipClientBean);
    }

}
