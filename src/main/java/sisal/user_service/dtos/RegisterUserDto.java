package sisal.user_service.dtos;

import java.util.Date;

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
    private String password;

}
