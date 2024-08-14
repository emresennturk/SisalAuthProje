package sisal.user_service.bootstrap;

import java.util.Date;
import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import sisal.user_service.dtos.RegisterUserDto;
import sisal.user_service.entities.Role;
import sisal.user_service.entities.RoleEnum;
import sisal.user_service.entities.User;
import sisal.user_service.repositories.RoleRepository;
import sisal.user_service.repositories.UserRepository;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent>{


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public AdminSeeder(
        RoleRepository roleRepository,
        UserRepository  userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    private void createSuperAdministrator() {
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setName("Admin");
        userDto.setSurname("admin");
        userDto.setBirthDate(new Date());
        userDto.setCountryOfBirth("TR");
        userDto.setEmail("super.admin@email.com");
        userDto.setPassword("12345667");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setBirthDate(userDto.getBirthDate());
        user.setCountryOfBirth(userDto.getCountryOfBirth());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
