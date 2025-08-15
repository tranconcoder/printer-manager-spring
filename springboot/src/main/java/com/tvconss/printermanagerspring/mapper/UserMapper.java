package com.tvconss.printermanagerspring.mapper;

import com.tvconss.printermanagerspring.dto.request.user.UpdateUserPatch;
import com.tvconss.printermanagerspring.dto.request.user.UpdateUserPut;
import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import com.tvconss.printermanagerspring.entity.UserEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.service.CloudinaryService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class UserMapper {

    @Autowired
    public CloudinaryService cloudinaryService;

    @Mapping(source = "firstName", target = "userFirstName")
    @Mapping(source = "lastName", target = "userLastName")
    @Mapping(source = "gender", target = "userGender", qualifiedByName = "genderStringToBoolean")
    public abstract void updateUserPatch(UpdateUserPatch updateUserFields, @MappingTarget UserEntity userEntity);

    @Mapping(source = "firstName", target = "userFirstName")
    @Mapping(source = "lastName", target = "userLastName")
    @Mapping(source = "gender", target = "userGender", qualifiedByName = "genderStringToBoolean")
    public abstract void updateUserPut(UpdateUserPut updateUserFields, @MappingTarget UserEntity userEntity);


    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "userEmail", target = "email")
    @Mapping(source = "userFirstName", target = "firstName")
    @Mapping(source = "userLastName", target = "lastName")
    @Mapping(source = "userGender", target = "gender", qualifiedByName = "genderBooleanToString")
    @Mapping(target = "avatars", expression = "java(cloudinaryService.getAvatarUrls(userEntity.getUserId()))" )
    public abstract UserResponse userEntityToUserResponse(UserEntity userEntity);



    @Named("genderBooleanToString")
    public String genderBooleanToString(boolean gender) {
        return gender ? "male" : "female";
    }

    @Named("genderStringToBoolean")
    public boolean genderStringToBoolean(String gender) {
        if (gender == null) {
            throw new ErrorResponse(ErrorCode.USER_UPDATE_INVALID_PAYLOAD);
        }

        return "male".equalsIgnoreCase(gender);
    }
}
