package com.example.rent_yourcomfort.service.impl;

import com.example.rent_yourcomfort.model.History;
import com.example.rent_yourcomfort.repository.HistoryRepository;
import com.example.rent_yourcomfort.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<History> getHistoryByUserId(Long userId) {
        return historyRepository.findByIdUser(userId);
    }

    @Override
    public History saveHistory(History history) {
        History historyEntity = new History();
        historyEntity.setId(history.getId());
        historyEntity.setIdUser(history.getIdUser());
        historyEntity.setHousingType(history.getHousingType());
        historyEntity.setStartDate(history.getStartDate());
        historyEntity.setEndDate(history.getEndDate());
        historyEntity.setBeds(history.getBeds());
        historyEntity.setBedRooms(history.getBedRooms());
        historyEntity.setBathRooms(history.getBathRooms());
        historyEntity.setMaxAmountPeople(history.getMaxAmountPeople());
        historyEntity.setCity(history.getCity());
        historyEntity.setDateOfRequest(new Date());

        return historyRepository.save(history);
    }
}

