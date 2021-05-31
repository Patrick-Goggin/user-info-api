package com.pgoggin.api.client.async

import com.pgoggin.api.domain.UserInfo
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

@Log
@Component("unregisteredUsersClient")
class UnregisteredUsersClient extends AsyncHttpClient<UserInfo> {
    @Value('${api.unregistered.users.uri}')
    String unregisteredUsersUri

    @Override
    CompletableFuture<HttpResponse<String>> getFuture() {
        super.get(unregisteredUsersUri)
    }

    @Override
    List<UserInfo> resolveFuture(CompletableFuture<HttpResponse<String>> future) {
        HttpResponse<String> response = null
        try {
            response = future.get()
        } catch (Exception e) {
            log.info("failed to get future")
        }
        List result = mapResponse(response.body())
        result
    }

    List<UserInfo> mapResponse(String responseBody) {
        super.mapResponse(responseBody)
    }
}
