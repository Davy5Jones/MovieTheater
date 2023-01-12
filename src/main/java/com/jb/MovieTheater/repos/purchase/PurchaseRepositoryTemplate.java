package com.jb.MovieTheater.repos.purchase;

import com.jb.MovieTheater.beans.mongo.Purchase;

public interface PurchaseRepositoryTemplate {
    Purchase invalidatePurchase(String purchaseId);
}
