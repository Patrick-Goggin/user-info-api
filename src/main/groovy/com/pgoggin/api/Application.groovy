package com.pgoggin.api

import groovy.util.logging.Log
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@Log
@SpringBootApplication
class Application {
    static void main(String[] args) {
        SpringApplication.run(Application, args)
    }
}
