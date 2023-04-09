package dev.ergo.practical.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.ergo.practical.model.Person;
import dev.ergo.practical.validator.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonDTO {

    private Integer id;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot be longer than 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot be longer than 50 characters")
    private String lastName;

    @Size(max = 20, message = "Gender name cannot be longer than 20 characters")
    @Gender
    private String gender;

    @NotNull(message = "Birth day is required")
    private Long birthDay;

    @Pattern(regexp = "^([0-9]{3}) ([0-9]{8})$", message = "Phone number is not valid")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;


    public Person fromDTO() {
        return new Person(
                this.getId(),
                this.getFirstName(),
                this.getLastName(),
                this.getGender(),
                new Timestamp(this.getBirthDay()),
                this.getPhoneNumber(),
                this.getEmail()
        );
    }
}
