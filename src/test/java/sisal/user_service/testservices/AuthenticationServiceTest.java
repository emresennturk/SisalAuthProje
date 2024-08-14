package sisal.user_service.testservices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import sisal.user_service.dtos.LoginUserDto;
import sisal.user_service.dtos.RegisterUserDto;
import sisal.user_service.entities.Role;
import sisal.user_service.entities.RoleEnum;
import sisal.user_service.entities.User;
import sisal.user_service.repositories.RoleRepository;
import sisal.user_service.repositories.UserRepository;
import sisal.user_service.services.AuthenticationService;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

   

    @Test
    void testSignupSuccess() throws ParseException {
        
        RegisterUserDto registerDto = new RegisterUserDto();
        registerDto.setName("Emre");
        registerDto.setSurname("Senturk");
    
        // String - Date dönüşümü
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse("2002-02-12");
        
        registerDto.setBirthDate(birthDate);
        registerDto.setCountryOfBirth("TR");
        registerDto.setEmail("emre.senturk@example.com");
        registerDto.setPassword("password");
        Role userRole = new Role();
        userRole.setName(RoleEnum.USER);

        when(roleRepository.findByName(RoleEnum.USER)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        
        User user = authenticationService.signup(registerDto);

        
        assertNotNull(user);
        assertEquals("Emre", user.getName());
        assertEquals("Senturk", user.getSurname());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(userRole, user.getRole());
    }

    @Test
    void testAuthenticateSuccess() {
        
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("emre.senturk@example.com");
        loginDto.setPassword("password");

        User user = new User();
        user.setEmail("emre.senturk@example.com");
        user.setPassword("encodedPassword");

        Authentication auth = mock(Authentication.class);

         when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        
        User authenticatedUser = authenticationService.authenticate(loginDto);

        
        assertNotNull(authenticatedUser);
        assertEquals("emre.senturk@example.com", authenticatedUser.getEmail());
    }

    
}
