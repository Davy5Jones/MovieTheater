package com.jb.MovieTheater.repos.logs;

import com.jb.MovieTheater.beans.mongo.CustomerLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.Instant;
@Repository
public class CustomerLogsRepositoryTemplateImpl implements CustomerLogsRepositoryTemplate {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void customerLogged(int customerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("customerId").is(customerId));
        Update update = new Update().set("lastSeen", Instant.now());
        mongoTemplate.findAndModify(query, update, CustomerLog.class);
    }
}
