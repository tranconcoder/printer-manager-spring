package com.tvconss.printermanagerspring.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Table(name = "user")
@Entity
@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

//    Auth information
    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "password", nullable = false)
    private String password;

//    User information
    @Column(name = "user_first_name", nullable = false)
    private String userFirstName;

    @Column(name = "user_last_name", nullable = false)
    private String userLastName;

    @Column(name = "user_gender", nullable = false)
    private boolean userGender;
}
