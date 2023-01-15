package com.jb.MovieTheater.repos.purchase;


import com.jb.MovieTheater.beans.mongo.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface PurchaseRepository extends MongoRepository<Purchase,String>, PurchaseRepositoryTemplate{
    Page<Purchase> findAllByUserId(int userId, Pageable pageable);

}
