package com.example.resy.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // Configure Redis connection details here
        config.useSingleServer().setAddress("redis://localhost:6379");

        return Redisson.create(config);
    }
}
