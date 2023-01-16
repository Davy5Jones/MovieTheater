package com.jb.MovieTheater.repos.purchase;

import com.jb.MovieTheater.beans.mongo.Purchase;

import java.util.Optional;

public interface PurchaseRepositoryTemplate {
    Optional<Purchase> invalidatePurchase(String purchaseId);
}
