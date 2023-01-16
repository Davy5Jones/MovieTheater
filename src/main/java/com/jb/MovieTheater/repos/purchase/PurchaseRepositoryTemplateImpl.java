package com.jb.MovieTheater.repos.purchase;

import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public class PurchaseRepositoryTemplateImpl implements PurchaseRepositoryTemplate{

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Optional<Purchase> invalidatePurchase(String purchaseId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(purchaseId));
        Update update = new Update().set("used",true);
        return Optional.ofNullable(mongoTemplate.findAndModify(query,update, FindAndModifyOptions.options().returnNew(true),Purchase.class));
    }
}
