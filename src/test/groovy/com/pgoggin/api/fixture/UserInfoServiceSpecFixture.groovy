package com.pgoggin.api.fixture


import com.pgoggin.api.domain.UserInfo

class UserInfoServiceSpecFixture {
    UserInfo userInfo1 = new UserInfo(id: "1", languageCode: "en", emailAddress: "email3@somewhere.com", registrationId: "jwsMJNOk3oM3hVM5bGcF3", registrationIdGeneratedTime: "156165026853", projectIds: ["1"])
    UserInfo userInfo2 = new UserInfo(id: "2", languageCode: "en", emailAddress: "email3@somewhere.com", registrationId: "jwsMJNOk3oM3hVM5bGcF3", registrationIdGeneratedTime: "156165026853", projectIds: ["2"])
    UserInfo userInfo3 = new UserInfo(id: "3", languageCode: "en", emailAddress: "email3@somewhere.com", registrationId: "jwsMJNOk3oM3hVM5bGcF3", registrationIdGeneratedTime: "156165026853", projectIds: ["3"])
    UserInfo userInfo4 = new UserInfo(id: "4", languageCode: "en", emailAddress: "email3@somewhere.com", registrationId: "jwsMJNOk3oM3hVM5bGcF3", registrationIdGeneratedTime: "156165026853", projectIds: ["4"])
    List<UserInfo> expectedList = [userInfo1, userInfo2, userInfo3, userInfo4]
}
