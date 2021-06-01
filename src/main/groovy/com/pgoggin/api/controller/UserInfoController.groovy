package com.pgoggin.api.controller

import com.pgoggin.api.domain.UserInfo

import com.pgoggin.api.service.ReactiveUserInfoService
import com.pgoggin.api.service.UserInfoService
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Slf4j
@RestController
class UserInfoController {
    @Autowired
    UserInfoService userInfoService
    @Autowired
    ReactiveUserInfoService reactiveUserInfoService2

    @GetMapping("/userinfo")
    List<UserInfo> getUserInfo() {
        userInfoService.getAllUserInfo()
    }

    @GetMapping("/reactive/userinfo")
    Mono<List<UserInfo>> getUserInfoReactive() {
        Mono<List<UserInfo>> result = reactiveUserInfoService2.getUserInfo()
        result
    }
}
