package com.jb.MovieTheater.repos.purchase;


import com.jb.MovieTheater.beans.mongo.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseRepository extends MongoRepository<Purchase,String>, PurchaseRepositoryTemplate{
    List<Purchase> findAllByUserId(int userId);

}
