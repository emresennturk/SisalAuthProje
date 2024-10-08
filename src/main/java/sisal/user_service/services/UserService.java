package sisal.user_service.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import sisal.user_service.dtos.RegisterUserDto;
import sisal.user_service.entities.Role;
import sisal.user_service.entities.RoleEnum;
import sisal.user_service.entities.User;
import sisal.user_service.repositories.RoleRepository;
import sisal.user_service.repositories.UserRepository;
import sisal.user_service.response.UserResponse;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> allUsers() {
        log.info("Fetching all users from the database");
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add); 
    
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(new UserResponse(user.getId(), user.getEmail())); 
        }
        log.info("Fetched {} users", userResponses.size());
        return userResponses; 
    }

    public User createAdministrator(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) {
            return null;
        }

        var user = new User();
        user.setName(input.getName());
        user.setSurname(input.getSurname());
        user.setBirthDate(input.getBirthDate());
        user.setCountryOfBirth(input.getCountryOfBirth());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(optionalRole.get());

        return userRepository.save(user);
    }
}
