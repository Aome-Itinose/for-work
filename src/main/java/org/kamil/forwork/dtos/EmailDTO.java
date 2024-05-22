package org.kamil.forwork.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDTO {

    @NotBlank(message = "Email have to be not blank.")
    @Email(message = "Invalid email")
    String email;

}