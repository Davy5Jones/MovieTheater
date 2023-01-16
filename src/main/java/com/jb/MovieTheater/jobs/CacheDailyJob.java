package com.jb.MovieTheater.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class CacheDailyJob {

    private final RedisCacheManager redisCacheManager;


}
