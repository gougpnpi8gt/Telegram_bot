package com.projectsvadim.vadimbot;

import com.projectsvadim.vadimbot.telegram.TelegramProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableConfigurationProperties(TelegramProperties.class)
@EnableAspectJAutoProxy
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
