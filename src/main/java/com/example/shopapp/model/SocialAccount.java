package com.example.shopapp.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="social_accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "provider",nullable = false,length = 20)
    private String provider;
    @Column(name = "provider_id",nullable = false,length = 50)
    private String providerId;
    @Column(name = "name",length = 150)
    private String name;
    @Column(name = "email",length = 150)
    private String email;

}
