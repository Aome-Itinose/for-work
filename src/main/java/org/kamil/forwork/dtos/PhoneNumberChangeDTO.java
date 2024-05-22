package org.kamil.forwork.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.kamil.forwork.security.PersonSecurityDetails;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneNumberChangeDTO {

    @NotBlank(message = "Number have to be not blank")
    @Pattern(regexp = "^\\+\\d{11}$", message = "Invalid phone number")
    String changeableNumber;

    @NotBlank(message = "Number have to be not blank")
    @Pattern(regexp = "^\\+\\d{11}$", message = "Invalid phone number")
    String newNumber;

    PersonSecurityDetails personDetails;

}
