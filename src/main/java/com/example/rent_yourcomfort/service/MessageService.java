package com.example.rent_yourcomfort.service;

import com.example.rent_yourcomfort.dto.MessageDTO;
import com.example.rent_yourcomfort.model.User;
import java.util.List;

public interface MessageService {

    void sendMessage(User sender, User recipient, String content);

    List<MessageDTO> getMessagesByUsers(User sender, User recipient);

    List<MessageDTO> getMessagesByBookingId(Long bookingId);

}
