package com.jb.MovieTheater;

import com.jb.MovieTheater.repos.logs.CustomerLogsRepository;
import com.jb.MovieTheater.repos.movie.MovieRepository;
import com.jb.MovieTheater.repos.purchase.PurchaseRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.Lifecycle;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.logging.Logger;

@SpringBootApplication
@EnableCaching
@EnableMongoRepositories
public class MovieTheaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieTheaterApplication.class, args);
    }

    @Component
    @RequiredArgsConstructor
    public static class ApplicationLifecycle implements Lifecycle {
        private final RedisCacheManager redisCacheManager;
        private final PurchaseRepository purchaseRepository;
        private final ScreeningRepository screeningRepository;
        private final MovieRepository movieRepository;
        private final TheaterRepository theaterRepository;
        private final CustomerLogsRepository customerLogsRepository;

        private final Logger logger = Logger.getLogger("logger");

        @Override
        public void start() {
            logger.info("Application start");
        }

        @Override
        public void stop() {
            logger.info("Application stop");
            purchaseRepository.deleteAll();
            screeningRepository.deleteAll();
            movieRepository.deleteAll();
            theaterRepository.deleteAll();
            redisCacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(redisCacheManager.getCache(name)).clear());

        customerLogsRepository.deleteAll();
        }

        @Override
        public boolean isRunning() {
            return true;
        }
    }
}
