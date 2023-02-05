package com.jb.MovieTheater.beans.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document("purchases")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Purchase {
    @MongoId
    private String id;
    private String userEmail;

    private int userId;
    private String screeningId;
    private Instant purchaseTime;
    private int rowId;
    private int seatId;

    private boolean used;

    public Purchase(String userEmail, int userId, String screeningId, Instant purchaseTime, int rowId, int seatId, boolean used) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.screeningId = screeningId;
        this.purchaseTime = purchaseTime;
        this.rowId = rowId;
        this.seatId = seatId;
        this.used = used;
    }
}
