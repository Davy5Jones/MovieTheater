package com.jb.MovieTheater.models.user;

import com.jb.MovieTheater.beans.SuperBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClerkModelDto extends SuperBean {
    private int id;
    private String emailAddress;
    private String clerkName;
}
