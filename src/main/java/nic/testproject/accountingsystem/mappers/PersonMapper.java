package nic.testproject.accountingsystem.mappers;

import nic.testproject.accountingsystem.dtos.administration.PersonDTO;
import nic.testproject.accountingsystem.dtos.authorization.RegisterResponseDTO;
import nic.testproject.accountingsystem.models.user.Person;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    RegisterResponseDTO personToRegisterResponseDTO(Person person);

    @Named(value = "personToDTO")
    PersonDTO personToDTO(Person person);

    @IterableMapping(qualifiedByName = "personToDTO")
    Set<PersonDTO> personToDTOSet(Page<Person> persons);
}
