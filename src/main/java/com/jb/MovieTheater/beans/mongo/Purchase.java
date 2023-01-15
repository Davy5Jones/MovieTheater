package com.jb.MovieTheater.beans.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;

@Document("purchases")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Purchase {
    @MongoId
    private String id;
    private int userId;
    private String screeningId;
    private int rowId;
    private int seatId;

    private boolean used;

    public Purchase(int userId, String screeningId, int rowId, int seatId) {
        this.userId = userId;
        this.screeningId = screeningId;
        this.rowId = rowId;
        this.seatId = seatId;
    }
}
