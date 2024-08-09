package sisal.user_service.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sisal.user_service.dtos.RegisterUserDto;
import sisal.user_service.entities.User;
import sisal.user_service.response.UserResponse;
import sisal.user_service.services.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createAdministrator(@RequestBody RegisterUserDto registerUserDto) {
        User createdAdmin = userService.createAdministrator(registerUserDto);
        return ResponseEntity.ok(createdAdmin);
    }

    @GetMapping("/getUsers")
@PreAuthorize("hasAnyRole('ADMIN')")
public ResponseEntity<List<UserResponse>> allUsers() {
    List<UserResponse> users = userService.allUsers();
    return ResponseEntity.ok(users);
}
}
