package com.example.rent_yourcomfort.controller;

import com.example.rent_yourcomfort.dto.MessageDTO;
import com.example.rent_yourcomfort.model.Booking;
import com.example.rent_yourcomfort.model.MessageRequest;
import com.example.rent_yourcomfort.model.User;
import com.example.rent_yourcomfort.repository.BookingRepository;
import com.example.rent_yourcomfort.repository.UserRepository;
import com.example.rent_yourcomfort.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
public class MessageController {

    private final MessageService messageService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public MessageController(MessageService messageService, BookingRepository bookingRepository, UserRepository userRepository) {
        this.messageService = messageService;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageRequest messageRequest) {
        User sender = userRepository.findById(messageRequest.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + messageRequest.getSenderId()));
        User recipient = userRepository.findById(messageRequest.getRecipientId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + messageRequest.getRecipientId()));
        if (messageRequest.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message cannot be empty! ");
        } else {
            messageService.sendMessage(sender, recipient, messageRequest.getContent());
            return ResponseEntity.ok("Message sent successfully");
        }
    }

    @GetMapping("/{user1Id}/{user2Id}")
    public List<MessageDTO> getMessagesByUsers(@PathVariable Long user1Id, @PathVariable Long user2Id) {
        User sender = new User();
        sender.setId(user1Id);

        User recipient = new User();
        recipient.setId(user2Id);

        return messageService.getMessagesByUsers(sender, recipient);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getMessagesByUsers(@PathVariable Long bookingId) {

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            List<MessageDTO> messages = messageService.getMessagesByBookingId(bookingId);
            return ResponseEntity.ok(messages);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking cannot be empty!");
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<String>> messages() {
        return ResponseEntity.ok(Arrays.asList("first", "second"));
    }
}
