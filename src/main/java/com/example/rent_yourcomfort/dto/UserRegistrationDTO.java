package com.example.rent_yourcomfort.dto;

import jakarta.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class UserRegistrationDTO {

    @NotEmpty
    private String firstName;
    private String userName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;
    @NotEmpty
    private String phone;

    private WalletDTO walletDTO;

}
