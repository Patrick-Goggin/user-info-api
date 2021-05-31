package com.pgoggin.api.client.async

import com.pgoggin.api.domain.UserInfo
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

@Log
@Component("registeredUsersClient")
class RegisteredUsersClient extends AsyncHttpClient<UserInfo> {
    @Value('${api.registered.users.uri}')
    String registeredUsersUri

    @Override
    CompletableFuture<HttpResponse<String>> getFuture() {
        super.get(registeredUsersUri)
    }

    @Override
    List<UserInfo> resolveFuture(CompletableFuture<HttpResponse<String>> future) {
        HttpResponse<String> response = null
        try {
            response = future.get()
        } catch (Exception e) {
            log.debug("failed to get future")
        }
        List result = mapResponse(response.body())
        result
    }

    List<UserInfo> mapResponse(String responseBody) {
        super.mapResponse(responseBody)
    }
}
