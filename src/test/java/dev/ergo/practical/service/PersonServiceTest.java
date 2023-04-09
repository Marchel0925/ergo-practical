package dev.ergo.practical.service;

import dev.ergo.practical.model.Person;
import dev.ergo.practical.repository.PersonRepository;
import dev.ergo.practical.service.person.PersonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {

    // Mock the dependency that the service uses
    @Mock
    private PersonRepository personRepository;

    // Inject the mock dependency into the service
    @InjectMocks
    private PersonServiceImpl personService;

    private List<Person> fakePeople;

    @Before
    public void init() {
        log.info("Populating people list");
        fakePeople = new ArrayList<>();
        fakePeople.add(new Person(1, "Gail", "Bowery", "male", new Timestamp(2014, 6, 24, 22, 12, 31, 0), "824 64846235", "gbowery0@geocities.jp"));
        fakePeople.add(new Person(2, "Cristobal", "Piddocke", "male", new Timestamp(2017, 11, 25, 19, 58, 19, 0), "113 91250992", "cpiddocke1@posterous.com"));
        fakePeople.add(new Person(3, "Ermengarde", "Mettricke", "female", new Timestamp(2022, 1, 27, 7, 15, 35, 0), "940 38761611", "emettricke2@theglobeandmail.com"));
    }

    @Test
    public void givenPersonService_whenFindAllIsCalled_thenServiceShouldReturnPeopleList() {
        when(personRepository.findAll()).thenReturn(fakePeople);
        List<Person> people = personService.findAll();
        Assertions.assertNotNull(people);
        Assertions.assertEquals(people.size(), 3);
    }

    @Test
    public void givenPersonService_whenFindByBirthdayCalled_thenServiceShouldReturnPeopleList() {
        Person p = fakePeople.get(0);
        when(personRepository.findByBirthDay(p.getBirthDay())).thenReturn(List.of(p));
        List<Person> people = personService.findByBirthDay(p.getBirthDay());
        Assertions.assertNotNull(people);
        Assertions.assertEquals(people.size(), 1);
        Assertions.assertEquals(people.get(0).getFirstName(), p.getFirstName());
    }

    @Test
    public void givenPersonService_whenFindByFirstNameIsCalled_thenServiceShouldReturnPeopleList() {
        Person p = fakePeople.get(0);
        when(personRepository.findByFirstName(p.getFirstName())).thenReturn(List.of(p));
        List<Person> people = personService.findByFirstName(p.getFirstName());
        Assertions.assertNotNull(people);
        Assertions.assertEquals(people.size(), 1);
        Assertions.assertEquals(people.get(0).getFirstName(), p.getFirstName());
    }

    @Test
    public void givenPersonService_whenSaveIsCalled_thenServiceShouldAPerson() {
        Person p = fakePeople.get(0);
        Person p1 = fakePeople.get(0);
        p.setId(0);
        when(personRepository.save(p)).thenReturn(p1);
        Person person = personService.saveOrUpdate(p);
        Assertions.assertNotNull(person);
        Assertions.assertEquals(person, p1);
        Assertions.assertEquals(person.getFirstName(), p1.getFirstName());
    }
}
