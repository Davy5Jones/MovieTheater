package com.jb.MovieTheater.repos.screening;

import com.jb.MovieTheater.beans.mongo.Screening;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;

@RequiredArgsConstructor
public class ScreeningRepositoryImpl implements ScreeningRepositoryTemplate {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void inactivateOldScreenings() {
        Query query = new Query();
        query.addCriteria(Criteria.where("screenTime").lte(Instant.now()));
        Update update = new Update().set("isActive",false);
        mongoTemplate.updateMulti(query,update,Screening.class);
    }

    @Override
    public Screening inactivateScreening(String screeningId){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(screeningId)).limit(1);
        Update update = new Update().set("isActive",false);
        return mongoTemplate.findAndModify(query,update,FindAndModifyOptions.options().returnNew(true), Screening.class);
    }

    @Override
    public void updateScreeningsDurationByMovieName(String movieName,int duration){
        Query query = new Query();
        query.addCriteria(Criteria.where("movieName").is(movieName));
        Update update = new Update().set("duration",duration);
        mongoTemplate.updateMulti(query,update,Screening.class);

    }



}
