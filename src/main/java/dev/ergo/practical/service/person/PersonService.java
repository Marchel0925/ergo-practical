package dev.ergo.practical.service.person;

import dev.ergo.practical.model.Person;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface PersonService {

    List<Person> findByFirstName(String firstName);

    List<Person> findByBirthDay(Timestamp birthDayTimestamp);

    List<Person> findByFirstNameAndBirthDay(String firstName, Timestamp birthDay);

    Person saveOrUpdate(Person person);

    void deleteById(Integer id);

    List<Person> findAll();


}
