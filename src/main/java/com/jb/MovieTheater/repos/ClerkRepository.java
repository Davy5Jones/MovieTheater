package com.jb.MovieTheater.repos;

import com.jb.MovieTheater.beans.mysql.Clerk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClerkRepository extends JpaRepository<Clerk,Integer> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email,int clerkId);

    Optional<Clerk> findByEmailAndPassword(String email, String password);

    Optional<Clerk> findByEmail(String email);

}
