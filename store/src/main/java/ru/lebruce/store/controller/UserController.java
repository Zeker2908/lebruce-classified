package ru.lebruce.store.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.lebruce.store.model.User;
import ru.lebruce.store.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> findAllUser() {
        return service.findAllUsers();
    }

    @PostMapping("save_user")
    public User saveUser(@RequestBody User user) {
        return service.saveUser(user);
    }

    @GetMapping("/{email}")
    public User findByEmail(@PathVariable String email) {
        return service.findByEmail(email);
    }

    @PutMapping("update_user")
    public User updateUser(@RequestBody User user) {
        return service.updateUser(user);
    }

    @DeleteMapping("delete_user_by_id/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
    }

    @DeleteMapping("delete_user_by_email/{email}")
    public void deleteUser(@PathVariable String email) {
        service.deleteUser(email);
    }
}