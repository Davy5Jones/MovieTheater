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

public class MovieRepositoryTemplateImpl implements MovieRepositoryTemplate {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public long inactivateMovie(String movieId) throws CustomCinemaException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(movieId));
        Update update = new Update().set("isActive", false);
        long modifiedCount = mongoTemplate.updateFirst(query, update, Movie.class).getModifiedCount();

        if (modifiedCount==0){
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST);
        }
        return modifiedCount;
    }

    @Override
    public int getMovieDurationByName(String movieName) throws CustomCinemaException {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(movieName)).fields().include("duration");
        Movie movie = mongoTemplate.findOne(query, Movie.class);
        if (movie != null) {
            return movie.getDuration();
        }
       throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST);
    }

@Override
    public String getMovieName(String movieId) throws CustomCinemaException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(movieId)).fields().include("name");
        Movie movie =mongoTemplate.findOne(query, Movie.class);
        if (movie==null){
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST);
        }
        return movie.getName();
    }

    @Override
    public Movie updateMovie(MovieModelDao movieModelDao, String movieId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(movieId));
        Update update = new Update().set("category",movieModelDao.getCategory())
                .set("description",movieModelDao.getDuration())
                .set("rating",movieModelDao.getRating());
        return mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),Movie.class);
    }
}
