package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.dto.request.user.UpdateUser;
import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import com.tvconss.printermanagerspring.entity.UserEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.enums.MediaSize;
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

        UserResponse userResponse = new UserResponse();
        userMapper.updateUserResponseFromEntity(userEntity, userResponse);

        return userResponse;
    }

    @Override
    @CachePut(value = "users", key = "#userId")
    public UserResponse updateUser(Long userId, UpdateUser updateUserFields) {
//        Find user by id
        UserEntity userEntity = this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new ErrorResponse(ErrorCode.USER_NOT_FOUND));

//        Merge update fields into user entity
        userMapper.updateUserFromDTO(updateUserFields, userEntity);

//        Save updated user entity
        this.userRepository.save(userEntity);

//        Return updated user response
        UserResponse userResponse = new UserResponse();
        userMapper.updateUserResponseFromEntity(userEntity, userResponse);

        return userResponse;
    }

    @Override
    public Map<String, String> getUserAvatarUrl(Long userId) {
        return this.cloudinaryService.getAvatarUrls(userId);
    }
}
