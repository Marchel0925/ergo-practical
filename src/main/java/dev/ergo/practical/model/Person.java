package dev.ergo.practical.model;

import dev.ergo.practical.dto.PersonDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;

// Lombok annotation to generate getters and setters, and also other class methods like toString, equals, hashCode, etc.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "people")
@Entity
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name= "gender")
    private String gender;

    @Column(name = "birth_day")
    private Timestamp birthDay;

    @Column(name= "phone_number")
    private String phoneNumber;

    @Column(name= "email")
    private String email;

    public PersonDTO toDTO() {
        return new PersonDTO(
                this.getId(),
                this.getFirstName(),
                this.getLastName(),
                this.getGender(),
                this.getBirthDay().getTime(),
                this.getPhoneNumber(),
                this.getEmail()
        );
    }
}
