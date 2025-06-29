package com.example.KhataWeb.Contoller;

import com.example.KhataWeb.Models.User;
import com.example.KhataWeb.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private  UserRepository userRepository;

    // ✅ Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // ✅ Set roles for a user
    @PutMapping("/users/{userId}/roles")
    public ResponseEntity<?> updateUserRoles(
            @PathVariable Long userId,
            @RequestBody Set<String> roles // e.g., ["ADMIN", "EMPLOYEE"]
    ) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok("User roles updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAUser(@PathVariable Long id){

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
