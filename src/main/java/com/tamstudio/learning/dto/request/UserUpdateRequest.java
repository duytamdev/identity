package com.tamstudio.learning.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Min(value = 6, message = "Password must be at least 6 characters")
    String password;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    List<String>roles;
}
