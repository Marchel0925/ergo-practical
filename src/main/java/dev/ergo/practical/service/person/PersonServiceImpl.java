package dev.ergo.practical.service.person;

import dev.ergo.practical.common.exception.ResourceNotFoundException;
import dev.ergo.practical.model.Person;
import dev.ergo.practical.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@AllArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    // Dependency injection
    // The lombok annotation @AllArgsConstructor will create a constructor for us
    // And Spring IoC will use it to inject the repository
    // As long as the defined repository is recognised by Spring IoC (annotated with @Repository, @Component, etc.)
    private final PersonRepository personRepository;

    @Override
    public List<Person> findByFirstName(String firstName) {
        requireNonNull(firstName, "First name cannot be null");
        log.info("Fetching people by first name containing: \"{}\"", firstName);
        return personRepository.findByFirstName(firstName);
    }

    @Override
    public List<Person> findByBirthDay(Timestamp birthDayTimestamp) {
        requireNonNull(birthDayTimestamp, "Birthday cannot be null");
        log.info("Fetching people by birthday: \"{}\"", birthDayTimestamp);
        return personRepository.findByBirthDay(birthDayTimestamp);
    }

    @Override
    public List<Person> findByFirstNameAndBirthDay(String firstName, Timestamp birthDay) {
        requireNonNull(firstName, "First name cannot be null");
        requireNonNull(birthDay, "Birthday cannot be null");
        log.info("Fetching people by name: \"{}\" and birthday: \"{}\"", firstName, birthDay);
        return personRepository.findByFirstNameStartsWithIgnoreCaseAndBirthDay(firstName, birthDay);
    }

    @Override
    public Person saveOrUpdate(Person person) {
        requireNonNull(person, "Person cannot be null");
        log.info("Saving person: \"{}\"", person.getEmail());
        return personRepository.save(person);
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id, "Id cannot be null");
        log.info("Deleting person by id: {}", id);
        personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person with id: \"" + id + "\" not found."));
        personRepository.deleteById(id);
    }

    @Override
    public List<Person> findAll() {
        log.info("Fetching all people");
        return personRepository.findAll();
    }
}
