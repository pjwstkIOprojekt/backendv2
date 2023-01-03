package com.gary.backendv2.model.security;

import com.gary.backendv2.model.users.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    private boolean valid;

    private LocalDateTime createdAt;
}
