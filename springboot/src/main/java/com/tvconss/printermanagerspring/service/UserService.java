package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.request.user.UpdateUser;
import com.tvconss.printermanagerspring.dto.response.user.UserResponse;

public interface UserService {

    public UserResponse getUserById(Long userId);

    public UserResponse updateUser(Long userId, UpdateUser updateUserFields);

    public String getUserAvatarUrl(Long userId);

}
