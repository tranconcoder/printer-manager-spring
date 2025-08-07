package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.dto.request.user.UpdateUser;
import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import com.tvconss.printermanagerspring.entity.UserEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.enums.MediaSize;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.repository.UserRepository;
import com.tvconss.printermanagerspring.service.CloudinaryService;
import com.tvconss.printermanagerspring.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public UserServiceImpl(UserRepository userRepository,
                           CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public UserResponse getUserById(Long userId) {
        UserEntity userEntity = this.userRepository.findByUserId(userId).orElse(null);

        if (userEntity == null) {
            throw new ErrorResponse(ErrorCode.USER_NOT_FOUND);
        }

        UserResponse userResponse = new UserResponse();
        userResponse.loadFromEntity(userEntity);
        userResponse.setAvatarUrl(this.cloudinaryService.getAvatarUrl(userId, MediaSize.AVATAR_SMALL));

        return userResponse;
    }

    @Override
    public void updateUser(UpdateUser updateUserFields) {

    }

    @Override
    public String getUserAvatarUrl(Long userId) {
        return "";
    }
}
