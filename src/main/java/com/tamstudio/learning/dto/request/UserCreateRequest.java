package com.tamstudio.learning.dto.request;

import com.tamstudio.learning.validator.AdultAge;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Size(min = 3, message = "Username must be at least 3 characters")
    String username;
    @Min(value = 6, message = "Password must be at least 6 characters")
    String password;
    String firstName;
    String lastName;
    @AdultAge(min = 18, message = "User must be at least 18 years old")
    LocalDate dateOfBirth;
}
