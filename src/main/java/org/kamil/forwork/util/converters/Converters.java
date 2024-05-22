package org.kamil.forwork.util.converters;

import lombok.RequiredArgsConstructor;
import org.kamil.forwork.dtos.EmailDTO;
import org.kamil.forwork.dtos.PersonDTO;
import org.kamil.forwork.dtos.PersonWithoutPasswordDTO;
import org.kamil.forwork.dtos.PhoneNumberDTO;
import org.kamil.forwork.store.entities.EmailEntity;
import org.kamil.forwork.store.entities.PersonEntity;
import org.kamil.forwork.store.entities.PhoneNumberEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Converters {

    private final ModelMapper modelMapper;

    public PersonEntity convertToEntity(PersonDTO personDTO){
        PersonEntity person = modelMapper.map(personDTO, PersonEntity.class);
        person.setAmountOfMoney((long) (personDTO.getAmountOfMoney() * 100));
        return person;
    }
    public PersonWithoutPasswordDTO convertToPersonDTO(PersonEntity personEntity){
        PersonWithoutPasswordDTO personDTO = modelMapper.map(personEntity, PersonWithoutPasswordDTO.class);
        personDTO.setAmountOfMoney((double) (personEntity.getAmountOfMoney() / 100));
        return personDTO;
    }
    public PhoneNumberEntity convertToPhoneNumberEntity(PhoneNumberDTO phoneNumberDTO){
        return modelMapper.map(phoneNumberDTO, PhoneNumberEntity.class);
    }
    public EmailEntity convertToEmailEntity(EmailDTO emailDTO){
        return modelMapper.map(emailDTO, EmailEntity.class);
    }
    public List<PersonWithoutPasswordDTO> convertToPersonDTOList(List<PersonEntity> personEntities){
        return personEntities.stream().map(this::convertToPersonDTO).toList();
    }
}
