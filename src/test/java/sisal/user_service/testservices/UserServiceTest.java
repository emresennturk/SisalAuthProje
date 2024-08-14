package sisal.user_service.testservices;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import sisal.user_service.dtos.RegisterUserDto;
import sisal.user_service.entities.Role;
import sisal.user_service.entities.RoleEnum;
import sisal.user_service.entities.User;
import sisal.user_service.repositories.RoleRepository;
import sisal.user_service.repositories.UserRepository;
import sisal.user_service.response.UserResponse;
import sisal.user_service.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private  PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void testAllUsers() {

    User user = new User();
    user.setId(UUID.randomUUID());
    user.setEmail("test@example.com");

    when(userRepository.findAll()).thenReturn(List.of(user));
    

    List<UserResponse> users = userService.allUsers();

    assertNotNull(users);
    assertEquals(1, users.size());
    assertEquals("test@example.com", users.get(0).getEmail());
    }

    @Test
    void testCreateAdministrator() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setName("Admin");
        registerUserDto.setSurname("User");
        registerUserDto.setBirthDate(new Date());
        registerUserDto.setCountryOfBirth("country");
        registerUserDto.setEmail("admin@example.com");
        registerUserDto.setPassword("password");
    
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setName(RoleEnum.ADMIN);
    
        when(roleRepository.findByName(RoleEnum.ADMIN)).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
    
        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setEmail("admin@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(adminRole);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        User createdAdmin = userService.createAdministrator(registerUserDto);
    
        assertNotNull(createdAdmin);
        assertEquals("admin@example.com", createdAdmin.getEmail());
        assertEquals("encodedPassword", createdAdmin.getPassword());
        assertEquals(adminRole, createdAdmin.getRole());
    
        // passwordEncoder.encode() işlevinin doğru parolayla çağrıldığını doğrulama
        verify(passwordEncoder).encode("password");
    }
}
