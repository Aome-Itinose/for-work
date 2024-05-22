package org.kamil.forwork.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.kamil.forwork.security.PersonSecurityDetails;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailChangeDTO {

    @NotBlank(message = "Email have to be not blank.")
    @Email(message = "Invalid email")
    String changeableEmail;

    @NotBlank(message = "Email have to be not blank.")
    @Email(message = "Invalid email")
    String newEmail;

    PersonSecurityDetails personDetails;
}
