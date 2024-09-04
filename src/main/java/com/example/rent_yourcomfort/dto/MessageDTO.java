package com.example.rent_yourcomfort.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class MessageDTO {
    private Long senderId;
    private Long recipientId;
    private String senderUsername;
    private String recipientUsername;
    private String text;
    private LocalDateTime timestamp;
}
