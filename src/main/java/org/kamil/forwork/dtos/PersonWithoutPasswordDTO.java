package org.kamil.forwork.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonWithoutPasswordDTO {
    @NotBlank(message = "Username have to be not blank.")
    String username;

    String fullName;

    Date bornAt;

    @Valid
    @NotEmpty(message = "Phone number have to be not empty")
    List<PhoneNumberDTO> phoneNumbers;

    @Valid
    @NotEmpty(message = "Email have to be not empty")
    List<EmailDTO> emails;

    @NotNull(message = "Amount of money have to be not empty")
    @Min(value = 0, message = "Money can't be less than 0")
    Double amountOfMoney;
}