package dev.ergo.practical.repository;

import dev.ergo.practical.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

// Spring Data JPA will create an implementation of this interface at runtime
// This will give us the option to call database based methods
// such as findAll(), save(), delete(), etc.
// We can also use HQL or JPQL to create our own methods
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    // Example of JPQL
    // Find by name and ignore case
    @Query("SELECT p FROM Person p WHERE lower(p.firstName) LIKE lower(concat(:name, '%'))")
    List<Person> findByFirstName(@Param("name") String firstName);

    List<Person> findByBirthDay(Timestamp birthDayTimestamp);

    List<Person> findByFirstNameStartsWithIgnoreCaseAndBirthDay(String firstName, Timestamp birthDay);

    // Find all comes by default
    // For a more performance oriented approach, we can also implement pagination

    // Save, update and delete comes by default
}
