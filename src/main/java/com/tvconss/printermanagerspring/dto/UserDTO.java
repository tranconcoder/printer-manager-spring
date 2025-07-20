package com.tvconss.printermanagerspring.dto;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "user")
@Entity
@Data
public class UserDTO {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

//    Auth information
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

//    User information
    @Column(name = "user_first_name", nullable = false)
    private String userFirstName;

    @Column(name = "user_last_name", nullable = false)
    private String userLastName;

    @Column(name = "user_gender", nullable = false)
    private String userGender;

    @Column(name = "user_address", nullable = false)
    private Integer userAddress;

    @Column(name = "user_birthday")
    private String userBirthday;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "user_phonenumber", unique = true, length = 10)
    private String userPhoneNumber;
}
