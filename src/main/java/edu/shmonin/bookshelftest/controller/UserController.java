package edu.shmonin.bookshelftest.controller;

import edu.shmonin.bookshelftest.model.User;
import edu.shmonin.bookshelftest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        var uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }
}