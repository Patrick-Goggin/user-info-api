package com.pgoggin.api.client.async

import com.pgoggin.api.domain.ProjectMembership
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

@Log
@Component("projectMembershipClient")
class ProjectMembershipClient extends AsyncHttpClient<ProjectMembership> {
    @Value('${api.project.memberships.uri}')
    String projectMembershipsUri

    @Override
    CompletableFuture<HttpResponse<String>> getFuture() {
        super.get(projectMembershipsUri)
    }

    @Override
    List<ProjectMembership> resolveFuture(CompletableFuture<HttpResponse<String>> future) {
        HttpResponse<String> response = null
        try {
            response = future.get()
        } catch (Exception e) {
            log.debug("failed to get future")
        }
        List result = mapResponse(response.body())
        result
    }

    List<ProjectMembership> mapResponse(String responseBody) {
        super.mapResponse(responseBody)
    }
}
