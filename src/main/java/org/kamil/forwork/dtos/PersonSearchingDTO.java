package org.kamil.forwork.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.kamil.forwork.store.entities.EmailEntity;
import org.kamil.forwork.store.entities.PhoneNumberEntity;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonSearchingDTO {
    String fullName;
    PhoneNumberEntity phoneNumber;
    EmailEntity email;
    Date burnAt;
}
