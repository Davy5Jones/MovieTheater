package com.jb.MovieTheater.repos.screening;

import com.jb.MovieTheater.beans.mongo.Screening;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScreeningRepositoryImpl implements ScreeningRepositoryTemplate {
    private final MongoTemplate mongoTemplate;


    @Override
    public void inactivateOldScreenings() {
        Query query = new Query();
        query.addCriteria(Criteria.where("screenTime").lte(Instant.now()));
        Update update = new Update().set("active", false);
        mongoTemplate.updateMulti(query, update, Screening.class);
    }

    @Override
    public Optional<Screening> inactivateScreening(String screeningId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(screeningId)).limit(1);
        Update update = new Update().set("active", false);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Screening.class));
    }

    @Override
    public void updateScreeningsDurationByMovieId(String movieId, int duration) {
        Query query = new Query();
        query.addCriteria(Criteria.where("movieId").is(movieId));
        Update update = new Update().set("duration", duration);
        mongoTemplate.updateMulti(query, update, Screening.class);

    }


}
