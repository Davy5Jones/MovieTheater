package com.jb.MovieTheater.repos;

import com.jb.MovieTheater.beans.mysql.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "SELECT email FROM movietheater.customers where id=?",nativeQuery = true)
    String getEmailById(int customerId);

    boolean existsByEmail(String email);
    Optional<Customer> findByEmail(String email);
    @Query(value = "SELECT id FROM movietheater.customers where email=?",nativeQuery = true)
    int getIdByEmail(String email);


}
