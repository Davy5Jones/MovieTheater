package com.jb.MovieTheater.beans.mysql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "customers")
@NoArgsConstructor
public class Customer extends User{

    @Builder
    public Customer(int id, @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email address") String email, @NotBlank(message = "Name cannot be empty") String name, @NotBlank(message = "Password is required") String password) {
        super(id, email, name, password);
    }
}
