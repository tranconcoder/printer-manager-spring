package com.tvconss.printermanagerspring.dto.response.user;

import com.tvconss.printermanagerspring.entity.UserEntity;
import lombok.Data;

@Data
public class User {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    public void loadFromEntity(UserEntity userEntity) {
        String userGenderStr = userEntity.isUserGender() ? "male" : "female";

        this.setUserId(userEntity.getUserId());
        this.setEmail(userEntity.getUserEmail());
        this.setFirstName(userEntity.getUserFirstName());
        this.setLastName(userEntity.getUserLastName());
        this.setGender(userGenderStr);
    }
}
