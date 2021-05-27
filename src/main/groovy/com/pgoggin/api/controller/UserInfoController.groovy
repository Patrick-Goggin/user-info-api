package com.pgoggin.api.controller

import com.pgoggin.api.domain.UserInfo
import com.pgoggin.api.service.UserInfoService
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class UserInfoController {
    @Autowired
    UserInfoService userInfoService
    @GetMapping("/userinfo")
    List<UserInfo> getUserInfo() {
        userInfoService.getAllUserInfo()
    }
}
