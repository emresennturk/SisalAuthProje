package sisal.user_service.dtos;

import java.util.Date;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterUserDto {

    private String name;
    private String surname;
    private Date birthDate;
    private String countryOfBirth;
    private String email;
    @Size(min = 8, message="Password must be at least 8 characters long")
    private String password;

}
