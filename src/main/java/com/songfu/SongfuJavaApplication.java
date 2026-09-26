package com.songfu;

import com.songfu.config.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties({FileProperties.class})
@EnableAsync
public class SongfuJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongfuJavaApplication.class, args);
    }

}
