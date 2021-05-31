package com.pgoggin.api.client.async

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

@Log
@Component
abstract class AsyncHttpClient<T> {
    @Value('${api.base.url}')
    String apiBaseUrl

    abstract CompletableFuture<HttpResponse<String>> getFuture()

    abstract List<T> resolveFuture(CompletableFuture<HttpResponse<String>> future)

    CompletableFuture<HttpResponse<String>> get(String endpointUri) {
        String url = "${apiBaseUrl}${endpointUri}"
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build()
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .GET().build()
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    }

    List mapResponse(String responseBody) {
        if (StringUtils.isEmpty(responseBody)) {
            return new ArrayList<>()
        }
        ObjectMapper objectMapper = new ObjectMapper()
        List result = objectMapper.readValue(responseBody, List)
        result
    }
}
