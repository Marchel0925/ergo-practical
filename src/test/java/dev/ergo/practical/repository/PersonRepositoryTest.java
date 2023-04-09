package dev.ergo.practical.repository;

import dev.ergo.practical.model.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@DataJpaTest
@SqlGroup({
        @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/schema.sql"),
        @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/data.sql")
})
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    private Person person;

    @Before
    public void init() {
        person = new Person(1, "Gail", "Bowery", "male", new Timestamp(2014, 6, 24, 22, 12, 31, 0), "824 64846235", "gbowery0@geocities.jp");
    }

    @Test
    public void givenPersonRepository_whenFindAllIsCalled_thenMethodShouldReturn20Entities() {
        List<Person> people = personRepository.findAll();
        Assertions.assertEquals(people.size(), 20);
    }

    @Test
    public void givenPersonRepository_whenFindByFirstNameIsCalled_thenMethodShouldListOfPeople() {
        // This method returns a list of people whose first name contains or starts with the given letter
        // It's ignore case so the people should be returned regardless of the case
        List<Person> people = personRepository.findByFirstName("g");
        Assertions.assertEquals(people.size(), 1);
        Assertions.assertTrue(people.get(0).getFirstName().toLowerCase().startsWith("g"));

        people = personRepository.findByFirstName("m");
        Assertions.assertEquals(people.size(), 3);
        for (Person person : people) {
            Assertions.assertTrue(person.getFirstName().toLowerCase().startsWith("m"));
        }

        people = personRepository.findByFirstName("L");
        Assertions.assertEquals(people.size(), 4);
        for (Person person : people) {
            Assertions.assertTrue(person.getFirstName().startsWith("L"));
        }

        people = personRepository.findByFirstName("Cristy");
        Assertions.assertEquals(people.size(), 1);
        Assertions.assertEquals(people.get(0).getFirstName(), "Cristy");
    }

    @Test
    public void givenPersonRepository_whenFindByBirthDayIsCalled_thenMethodShouldReturnPeopleList() {
        // https://www.epochconverter.com/ (Local time)
        // Id: 1 = 1403637151000
        Timestamp ts = new Timestamp(1403637151000L);
        List<Person> people = personRepository.findByBirthDay(ts);
        Assertions.assertEquals(people.size(), 1);
        Assertions.assertEquals(people.get(0).getFirstName(), "Gail");
        Assertions.assertEquals(ts.getTime(), people.get(0).getBirthDay().getTime());
    }

    @Test
    public void givenPersonRepository_whenSaveIsCalled_thenMethodShouldReturnSavedPerson() {
        person.setId(0);
        person = personRepository.save(person);
        Assertions.assertEquals(person.getId(), 21);

        List<Person> people = personRepository.findAll();
        Assertions.assertEquals(people.size(), 21);
    }

    @Test
    public void givenPersonRepository_whenUpdateIsCalled_thenMethodShouldReturnPeopleList() {
        person.setEmail("someemail@gmail.com");
        person = personRepository.save(person);

        Assertions.assertEquals(person.getId(), 1);
        Assertions.assertEquals(person.getEmail(), "someemail@gmail.com");

        List<Person> people = personRepository.findAll();
        Assertions.assertEquals(people.size(), 20);
    }

    @Test
    public void givenPersonRepository_whenDeleteIsCalled_thenMethodShouldDeleteThePerson() {
        personRepository.deleteById(person.getId());

        List<Person> people = personRepository.findAll();
        Assertions.assertEquals(people.size(), 19);

        person = personRepository.findById(1).orElse(null);
        Assertions.assertNull(person);
    }
}
