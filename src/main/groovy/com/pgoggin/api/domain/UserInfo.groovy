package com.pgoggin.api.domain

import com.fasterxml.jackson.annotation.JsonInclude

class UserInfo {
    UserInfo() {
        this.projectIds = getProjectIds() == null ? new ArrayList<>() : getProjectIds()
    }

    UserInfo(String id, String city, String company, String country, String firstName, String lastName, String organizationType, String phone, String state, String zipCode, Boolean disclaimerAccepted, String languageCode, String emailAddress, String registrationId, String registrationIdGeneratedTime, List<String> projectIds) {
        this.id = id
        this.city = city
        this.company = company
        this.country = country
        this.firstName = firstName
        this.lastName = lastName
        this.organizationType = organizationType
        this.phone = phone
        this.state = state
        this.zipCode = zipCode
        this.disclaimerAccepted = disclaimerAccepted
        this.languageCode = languageCode
        this.emailAddress = emailAddress
        this.registrationId = registrationId
        this.registrationIdGeneratedTime = registrationIdGeneratedTime
        this.projectIds = projectIds == null ? new ArrayList<>() : projectIds
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

    void addProjectId(String projectId) {
        if (getProjectIds() == null) {
            setProjectIds(new ArrayList<>())
        }
        getProjectIds().add(projectId)
    }
}
