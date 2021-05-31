package com.pgoggin.api.domain

import com.fasterxml.jackson.annotation.JsonInclude

class UserInfo {
    UserInfo() {
        this.projectIds = getProjectIds() == null ? new ArrayList<>() : getProjectIds() as List<String>
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String id
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String city
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String company
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String country
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String firstName
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String lastName
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String organizationType
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String phone
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String state
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String zipCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean disclaimerAccepted
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String languageCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String emailAddress
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String registrationId
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String registrationIdGeneratedTime
    @JsonInclude(JsonInclude.Include.ALWAYS)
    List<String> projectIds
}
