package com.jb.MovieTheater.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClerkModelDto {
    private int id;
    private String emailAddress;
    private String clerkName;
}
