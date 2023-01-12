package com.jb.MovieTheater.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginBodyModel {

    private String email;
    private String password;
    private boolean stayLoggedIn;
}
