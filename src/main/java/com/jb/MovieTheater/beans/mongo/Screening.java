package com.jb.MovieTheater.beans.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Document("screenings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Screening {
    @MongoId
    private String id;
    private String movieName;
    private Instant screenTime;
    private List<boolean[]> seats;
    private String theaterId;
    private int duration;
    private boolean is3D;
    private boolean isActive;

}
