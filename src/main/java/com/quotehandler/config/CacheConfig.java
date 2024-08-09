package com.quotehandler.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.Collections;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CaffeineCache caffeineCacheConfig() {
        return new CaffeineCache("customerCache", Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10))
                .initialCapacity(1)
                .maximumSize(2000)
                .build());
    }

    @Bean
    @Primary
    public CacheManager caffeineCacheManager(CaffeineCache caffeineCache) {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Collections.singletonList(caffeineCache));
        return manager;
    }
}
