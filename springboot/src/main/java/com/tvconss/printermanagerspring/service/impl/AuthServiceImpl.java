package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.dto.request.user.AuthLoginRequest;
import com.tvconss.printermanagerspring.dto.request.user.AuthRegisterRequest;
import com.tvconss.printermanagerspring.dto.response.user.AuthResponse;
import com.tvconss.printermanagerspring.dto.response.user.JwtTokenPair;
import com.tvconss.printermanagerspring.entity.UserEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.repository.UserRepository;
import com.tvconss.printermanagerspring.service.AuthService;
import com.tvconss.printermanagerspring.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private KeyPairGenerator keyPairGenerator;

    private AuthUtil authUtil;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthUtil authUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authUtil = authUtil;
    }


    public AuthResponse register(AuthRegisterRequest data) {
//        Check email is not existed
        String email = data.getEmail();
        UserEntity userExisted = this.userRepository.findByUserEmail(email);

        if (userExisted != null) {
            throw new ErrorResponse(ErrorCode.AUTH_USER_ALREADY_EXIST);
        }

//      Hash password
        String hashPassword = passwordEncoder.encode(data.getPassword());

//        Create new entity
        UserEntity userEntity = new UserEntity();
        boolean isMale = data.getGender().equals("male");

        userEntity.setPassword(hashPassword);
        userEntity.setUserEmail(email);
        userEntity.setUserFirstName(data.getFirstName());
        userEntity.setUserLastName(data.getLastName());
        userEntity.setUserGender(isMale);

        UserEntity savedUser = this.userRepository.save(userEntity);

        if (savedUser == null || savedUser.getUserId() == null) {
            throw new ErrorResponse(ErrorCode.AUTH_ERROR_INTERNAL);
        }

        return authUtil.generateAuthInformation(savedUser);
    }


    public AuthResponse login(AuthLoginRequest data) {
        String email = data.getEmail();
        String password = data.getPassword();

//        Check user is exist
        UserEntity user = this.userRepository.findByUserEmail(email);

        if (user == null) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED);
        }

//        Check password is matches?
        boolean isPasswordMatches = this.passwordEncoder.matches(password, user.getPassword());
        if (!isPasswordMatches) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED);
        }

        return authUtil.generateAuthInformation(user);
    }

    public JwtTokenPair refreshToken(String refreshToken) {
        return null;
    }
}
