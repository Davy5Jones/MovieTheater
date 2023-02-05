package com.jb.MovieTheater.repos.logs;

import com.jb.MovieTheater.beans.mongo.CustomerLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerLogsRepository extends MongoRepository<CustomerLog,String>,CustomerLogsRepositoryTemplate {
    Optional<CustomerLog> findCustomerLogByCustomerId(int customerId);
}
