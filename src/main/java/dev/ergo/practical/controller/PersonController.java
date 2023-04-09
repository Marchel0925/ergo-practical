package dev.ergo.practical.controller;

import dev.ergo.practical.common.response.SuccessResponse;
import dev.ergo.practical.dto.PersonDTO;
import dev.ergo.practical.model.Person;
import dev.ergo.practical.service.person.PersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/person")
@Validated
public class PersonController {

    private final PersonService personService;

    // This single endpoint can be used for multiple operations
    // 1. Get all persons
    // 2. Get persons by name
    // 3. Get persons by birthday
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<PersonDTO>>> getPersonsList(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "birthday", required = false) Long birthDay) {
        SuccessResponse<List<PersonDTO>> response;
        List<PersonDTO> personDTOList;
        if (name != null && birthDay != null) {
            personDTOList = personService.findByFirstNameAndBirthDay(name, new Timestamp(birthDay)).stream().map(Person::toDTO).toList();
            response = new SuccessResponse<>("People found by name and birthday.", personDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (name != null) {
            personDTOList = personService.findByFirstName(name).stream().map(Person::toDTO).toList();
            response = new SuccessResponse<>("People found by name.", personDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (birthDay != null) {
            // Transform Long milliseconds to sql timestamp
            personDTOList = personService.findByBirthDay(new Timestamp(birthDay)).stream().map(Person::toDTO).toList();
            response = new SuccessResponse<>("People found by birthday.", personDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            personDTOList = personService.findAll().stream().map(Person::toDTO).toList();
            response = new SuccessResponse<>("People list.", personDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<Person>> savePerson(@Valid @RequestBody PersonDTO personDTO) {
        // We set the ID to 0 to insert a new row in the database
        // Hibernate will automatically generate a new ID
        personDTO.setId(0);

        Person person = personService.saveOrUpdate(personDTO.fromDTO());
        // Construct response
        SuccessResponse<Person> response = new SuccessResponse<>("Person saved.", person);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<Person>> updatePerson(@Valid @RequestBody PersonDTO personDTO) {
        Person person = personService.saveOrUpdate(personDTO.fromDTO());
        SuccessResponse<Person> response = new SuccessResponse<>("Person updated.", person);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deletePerson(@PathVariable(name = "id") @Positive(message = "Id parameter has to be positive") Integer id) {
        personService.deleteById(id);
        return new ResponseEntity<>("Person deleted.", HttpStatus.OK);
    }
}
