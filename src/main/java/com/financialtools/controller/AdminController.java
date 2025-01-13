package com.financialtools.controller;

import com.financialtools.model.Transaction;
import com.financialtools.model.User;
import com.financialtools.repository.TransactionRepository;
import com.financialtools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @PutMapping("/users/{id}/roles")
    public User updateUserRoles(@PathVariable Long id, @RequestBody Set<String> roles) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRoles(roles);
                    return userRepository.save(user);
                }).orElse(null);
    }

    @PutMapping("/users/{id}/deactivate")
    public User deactivateUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(false); // Assuming there's an `active` field in User
                    return userRepository.save(user);
                }).orElse(null);
    }

    @PutMapping("/users/{id}/activate")
    public User activateUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(true); // Assuming there's an `active` field in User
                    return userRepository.save(user);
                }).orElse(null);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
