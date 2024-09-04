package com.example.rent_yourcomfort.controller;

import com.example.rent_yourcomfort.model.History;
import com.example.rent_yourcomfort.service.HistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{userId}")
    public List<History> getHistoryByUserId(@PathVariable Long userId) {
        return historyService.getHistoryByUserId(userId);
    }
}
