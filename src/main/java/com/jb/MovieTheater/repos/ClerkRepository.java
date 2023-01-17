package com.jb.MovieTheater.repos;

import com.jb.MovieTheater.beans.mysql.Clerk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClerkRepository extends JpaRepository<Clerk, Integer> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, int clerkId);
    Optional<Clerk> findByEmail(String email);

}
