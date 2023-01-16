package com.jb.MovieTheater.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jb.MovieTheater.models.movie.MovieModelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.serializer.*;

import java.time.*;
import java.util.ArrayList;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisCacheConfig {
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(24))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }




}
