package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.dto.request.user.UpdateUserPatch;
import com.tvconss.printermanagerspring.dto.request.user.UpdateUserPut;
import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import com.tvconss.printermanagerspring.entity.UserEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.mapper.UserMapper;
import com.tvconss.printermanagerspring.repository.UserRepository;
import com.tvconss.printermanagerspring.service.CloudinaryService;
import com.tvconss.printermanagerspring.service.UserService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           CloudinaryService cloudinaryService,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.userMapper = userMapper;
    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    public UserResponse getUserById(Long userId) {
        UserEntity userEntity = this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new ErrorResponse(ErrorCode.USER_NOT_FOUND));

        UserResponse userResponse = this.userMapper.userEntityToUserResponse(userEntity);

        return userResponse;
    }

    @Override
    @CachePut(value = "users", key = "#userId")
    public UserResponse updateUser(Long userId, UpdateUserPatch updateUserFields) {
//        Find user by id
        UserEntity userEntity = this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new ErrorResponse(ErrorCode.USER_NOT_FOUND));

//        Merge update fields into user entity
        userMapper.updateUserPatch(updateUserFields, userEntity);

//        Save updated user entity
        try {
            this.userRepository.save(userEntity);
        } catch (Exception e) {
            throw new ErrorResponse(ErrorCode.USER_ERROR_INTERNAL, "Error when updating user");
        }

//        Return updated user response
        UserResponse userResponse = this.userMapper.userEntityToUserResponse(userEntity);

        return userResponse;
    }

    @Override
    @CachePut(value = "users", key = "#userId")
    public UserResponse updateUserPut(Long userId, UpdateUserPut newUser) {
//        Find old user by id
        UserEntity user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ErrorResponse(ErrorCode.USER_NOT_FOUND));

//        Merge new user fields into old user entity
        this.userMapper.updateUserPut(newUser, user);

//        Save updated user entity
        try {
            this.userRepository.save(user);
        } catch(Exception e) {
            throw new ErrorResponse(ErrorCode.USER_ERROR_INTERNAL, "Error when updating user");
        }

//        Return updated user response
        return this.userMapper.userEntityToUserResponse(user);
    }

    @Override
    public Map<String, String> getUserAvatarUrl(Long userId) {
        return this.cloudinaryService.getAvatarUrls(userId);
    }
}
