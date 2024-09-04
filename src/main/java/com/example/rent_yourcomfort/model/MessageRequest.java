package com.example.rent_yourcomfort.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageRequest {
    private Long senderId;
    private Long recipientId;
    private String content;
}
