package com.jb.MovieTheater.repos.movie;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.movie.MovieModelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MovieRepositoryTemplateImpl implements MovieRepositoryTemplate {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Optional<Movie> inactivateMovie(String movieId) throws CustomCinemaException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(movieId));
        Update update = new Update().set("isActive", false);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, Movie.class));
    }

    @Override
    public Movie getMovieDurationAndNameById(String movieId) throws CustomCinemaException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(movieId)).fields().include("duration", "name");
        Movie movie = mongoTemplate.findOne(query, Movie.class);
        if (movie != null) {
            return movie;
        }
        throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST);
    }

    @Override
    public String getMovieName(String movieId) throws CustomCinemaException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(movieId)).fields().include("name");
        Movie movie = mongoTemplate.findOne(query, Movie.class);
        if (movie == null) {
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST);
        }
        return movie.getName();
    }

    @Override
    public Movie updateMovie(MovieModelDao movieModelDao, String movieId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(movieId));
        Update update = new Update().set("category", movieModelDao.getCategory())
                .set("description", movieModelDao.getDuration())
                .set("rating", movieModelDao.getRating());
        return mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Movie.class);
    }
}
