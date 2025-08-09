package com.tvconss.printermanagerspring.dto.response.user;

import com.tvconss.printermanagerspring.entity.UserEntity;
import com.tvconss.printermanagerspring.enums.MediaSize;
import com.tvconss.printermanagerspring.service.CloudinaryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String avatarUrl;

    @Autowired
    public CloudinaryService cloudinaryService;

    public void loadFromEntity(UserEntity userEntity) {
        String userGenderStr = userEntity.isUserGender() ? "male" : "female";

        this.setUserId(userEntity.getUserId());
        this.setEmail(userEntity.getUserEmail());
        this.setFirstName(userEntity.getUserFirstName());
        this.setLastName(userEntity.getUserLastName());
        this.setGender(userGenderStr);
    }

    public String getAvatarUrl() {
        Long userId = this.getUserId();

        if (userId == null) {
            return null;
        }

        return cloudinaryService.getAvatarUrl(userId, MediaSize.AVATAR_SMALL);
    }
}
