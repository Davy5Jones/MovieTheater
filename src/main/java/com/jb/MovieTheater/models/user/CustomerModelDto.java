package com.jb.MovieTheater.models.user;

import com.jb.MovieTheater.beans.SuperBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerModelDto extends SuperBean {
    private int id;
    private String emailAddress;
    private String customerName;
    private Instant registered;
    private Instant lastSeen;
}
