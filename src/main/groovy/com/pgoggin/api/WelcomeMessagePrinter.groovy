package com.pgoggin.api

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Log
import lombok.SneakyThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.util.StreamUtils

import static java.nio.charset.StandardCharsets.UTF_8

@Log
@Component
class WelcomeMessagePrinter {
    private static final int MB = 1024 * 1024
    @Value('${spring.application.name}')
    String springApplicationName
    @Value('${log.level}')
    String logLevel
    @Value('${spring.profiles.active}')
    String springProfilesActive
    ObjectMapper mapper

    @Autowired
    WelcomeMessagePrinter(ObjectMapper mapper) {
        this.mapper = mapper
    }

    void printFullWelcomeMessage() {
        this.printShortAppHeader()
        this.printAppHeader()
        this.printAppProperties()
        this.printMemoryUsage()
    }

    void printShortAppHeader() {
        log.info(String.format(' ** Application: {} Startup ** %s', springApplicationName))
    }

    @SneakyThrows
    void printAppHeader() {
        String appHeaderString = StreamUtils.copyToString(new ClassPathResource("welcomeMessageHeader.txt").getInputStream(), UTF_8)

        log.info('\n' + appHeaderString)
    }

    @SneakyThrows
    private void printAppProperties() {
        Map<String, Object> properties = new HashMap<>()
        properties.put("logLevel", logLevel)
        properties.put("springProfilesActive", springProfilesActive)
        log.info(String.format('\n{} %s', mapper.writerWithDefaultPrettyPrinter().writeValueAsString(properties)))
    }

    @SneakyThrows
    private void printMemoryUsage() {
        Runtime runtime = Runtime.getRuntime()
        Map<String, String> properties = new HashMap<>()
        properties.put("usedMemory", ((runtime.totalMemory() - runtime.freeMemory()) / MB) + "m")
        properties.put("freeMemory", (runtime.freeMemory() / MB) + "m")
        properties.put("totalMemory", (runtime.totalMemory() / MB) + "m")
        properties.put("maxMemory", (runtime.maxMemory() / MB) + "m")
        log.info(String.format('\n{} %s', mapper.writerWithDefaultPrettyPrinter().writeValueAsString(properties)))
    }

    @EventListener
    void onApplicationEvent(ContextRefreshedEvent event) {
        this.printFullWelcomeMessage()
    }
}
