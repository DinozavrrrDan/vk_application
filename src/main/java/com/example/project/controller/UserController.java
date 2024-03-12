package com.example.project.controller;

import com.example.project.exeption.BusinessException;
import com.example.project.model.comment.Comment;
import com.example.project.model.user.User;
import com.example.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USERS_ADMIN') || hasAuthority('ROLE_USERS_VIEWER')")
    public ResponseEntity<User[]> getUsers() throws BusinessException {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USERS_ADMIN') || hasAuthority('ROLE_USERS_VIEWER')")
    public ResponseEntity<User> getUser(@PathVariable String id) throws BusinessException {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/comments")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USERS_ADMIN') || hasAuthority('ROLE_USERS_VIEWER')")
    public ResponseEntity<Comment[]> getUserComments(@PathVariable String id) throws BusinessException {
        return new ResponseEntity<>(userService.getUserComments(id), HttpStatus.OK);
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USERS_ADMIN')")
    public ResponseEntity<User> postUser(@RequestBody User user) throws BusinessException {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USERS_ADMIN')")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String id) throws BusinessException {
        return new ResponseEntity<>(userService.updateUser(user, id), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USERS_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
