package com.pgoggin.api.client

import com.fasterxml.jackson.databind.ObjectMapper
import reactor.core.publisher.Flux

class MockReactiveClient<T> {
    String directory

    MockReactiveClient(String directory) {
        this.directory = directory
    }

    public <T> Flux<T> getMockResponseAsFlux(String fileName, Class<T> requiredType) {
        ObjectMapper objectMapper = new ObjectMapper()
        String fileContents = getFileContentsAsString('registeredusers-response.json')
        List<T> list = objectMapper.readValue(fileContents, List<T>.class)
        Flux.fromIterable(list) as Flux<T>
    }

    private String getFileContentsAsString(String fileName) {
        String filePath = "${this.directory}${fileName}"
        File file = new File(filePath)
        file.text
    }
}
