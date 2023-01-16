package com.jb.MovieTheater.repos.theater;

import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TheaterRepositoryTemplateImpl implements TheaterRepositoryTemplate {
    private final MongoTemplate mongoTemplate;

    @Override
    @Cacheable(cacheNames = "theaterName", value = "theaterName",key = "#theaterId")
    public String getTheaterNameById(String theaterId) {
        Query query = new Query();
        query.fields().include("name");
        query.addCriteria(Criteria.where("id").is(theaterId));
        Theater theater = mongoTemplate.findOne(query, Theater.class);
        if (theater == null) {
            return "";
        }
        return theater.getName();
    }
}
