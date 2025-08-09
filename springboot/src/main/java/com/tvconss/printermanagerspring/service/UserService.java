package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.request.user.UpdateUser;
import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import com.tvconss.printermanagerspring.enums.MediaSize;

import java.util.Map;

public interface UserService {

    public UserResponse getUserById(Long userId);

    public UserResponse updateUser(Long userId, UpdateUser updateUserFields);

    public Map<Integer, String> getUserAvatarUrl(Long userId);

}
