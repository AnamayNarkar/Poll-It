package com.implementation.PollingApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupMessageConfig {

    private final int port;

    public StartupMessageConfig(@Value("${server.port}") int port) {
        this.port = port;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        System.out.println("Server running on port " + port);
    }
}
