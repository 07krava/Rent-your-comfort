package com.example.rent_yourcomfort.service;

import com.example.rent_yourcomfort.model.History;
import java.util.List;

public interface HistoryService {

    List<History> getHistoryByUserId(Long userId);

    History saveHistory(History history);
}
