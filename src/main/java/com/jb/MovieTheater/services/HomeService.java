package com.jb.MovieTheater.services;

import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.user.CustomerModelDao;

public interface HomeService {

    void register(CustomerModelDao customerModelDao) throws CustomCinemaException;


}
