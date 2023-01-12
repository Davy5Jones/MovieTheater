package com.jb.MovieTheater.repos.purchase;

import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class PurchaseRepositoryTemplateImpl implements PurchaseRepositoryTemplate{

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Purchase invalidatePurchase(String purchaseId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(purchaseId));
        Update update = new Update().set("used",true);
        return mongoTemplate.findAndModify(query,update,Purchase.class);
    }
}
