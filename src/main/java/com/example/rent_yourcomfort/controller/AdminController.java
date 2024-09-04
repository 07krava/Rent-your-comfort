package com.example.rent_yourcomfort.controller;

import com.example.rent_yourcomfort.model.History;
import com.example.rent_yourcomfort.service.HistoryService;
import com.example.rent_yourcomfort.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final HistoryService historyService;

    @Autowired
    public AdminController(UserService userService, HistoryService historyService) {
        this.userService = userService;
        this.historyService = historyService;
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User with id: " + id + " delete successfully!", HttpStatus.OK);
    }

    @GetMapping("/history/{userId}")
    public List<History> getHistoryByUserId(@PathVariable Long userId) {
        return historyService.getHistoryByUserId(userId);
    }
}

