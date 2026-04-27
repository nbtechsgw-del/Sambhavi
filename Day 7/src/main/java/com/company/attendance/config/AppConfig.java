package com.company.attendance.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApplicationTimeProperties.class)
public class AppConfig {

    @Bean
    public Clock appClock(ApplicationTimeProperties properties) {
        return Clock.system(ZoneId.of(properties.getTimezone()));
    }
}
