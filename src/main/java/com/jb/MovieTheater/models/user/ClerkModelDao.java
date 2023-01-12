package com.jb.MovieTheater.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClerkModelDao {

    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email address")
    @NotBlank(message = "user email is required")
    private String emailAddress;
    @NotBlank(message = "Name cannot be empty")
    private String clerkName;
    @NotBlank(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
