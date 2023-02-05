package com.jb.MovieTheater.repos;

import com.jb.MovieTheater.beans.mysql.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "SELECT email FROM movietheater.customers where id=?", nativeQuery = true)
    Optional<String>  getEmailById(int customerId);

    boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);

    @Query(value = "SELECT id FROM movietheater.customers where email=?", nativeQuery = true)
    Optional<Integer> getIdByEmail(String email);


}
