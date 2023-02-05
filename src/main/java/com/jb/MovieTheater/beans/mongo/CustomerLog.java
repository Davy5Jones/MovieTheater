package com.jb.MovieTheater.beans.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
@Document("customerLogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLog {
    @MongoId
    private String id;
    private int customerId;
    private Instant registered;
    private Instant lastSeen;

    public CustomerLog(int customerId, Instant registered, Instant lastSeen) {
        this.customerId = customerId;
        this.registered = registered;
        this.lastSeen = lastSeen;
    }
}
