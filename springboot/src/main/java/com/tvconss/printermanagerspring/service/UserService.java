package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.request.user.UpdateUserPatch;
import com.tvconss.printermanagerspring.dto.request.user.UpdateUserPut;
import com.tvconss.printermanagerspring.dto.response.user.UserResponse;

import java.util.Map;

public interface UserService {

    public UserResponse getUserById(Long userId);

    public UserResponse updateUser(Long userId, UpdateUserPatch updateUserFields);

    public UserResponse updateUserPut(Long userId, UpdateUserPut newUser);

    public Map<String, String> getUserAvatarUrl(Long userId);

}
