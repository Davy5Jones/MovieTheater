package com.jb.MovieTheater.jobs;

import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class DailyJob {

    private final RedisCacheManager redisCacheManager;
    private final ScreeningRepository screeningRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void clearCacheJob() {
        redisCacheManager.getCacheNames()
                .forEach(name -> Objects.requireNonNull(redisCacheManager.getCache(name)).clear());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void inactivateOldScreeningsJob() {
        screeningRepository.inactivateOldScreenings();
    }


}
