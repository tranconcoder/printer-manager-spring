package com.tvconss.printermanagerspring.mapper;

import com.tvconss.printermanagerspring.dto.request.user.UpdateUser;
import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import com.tvconss.printermanagerspring.entity.UserEntity;
import com.tvconss.printermanagerspring.service.CloudinaryService;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "userFirstName", source = "firstName")
    @Mapping(target = "userLastName", source = "lastName")
    @Mapping(target = "userGender", source = "gender", qualifiedByName = "genderBooleanToString")
    @Mapping(target = "userEmail", source = "email")
    void updateUserFromDTO(UpdateUser updateUserFields, @MappingTarget UserEntity userEntity);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "email", source = "userEmail")
    @Mapping(target = "firstName", source = "userFirstName")
    @Mapping(target = "lastName", source = "userLastName")
    @Mapping(target = "firstName", source = "userGender")
    void updateUserResponseFromEntity(UserEntity userEntity, @MappingTarget UserResponse userResponse);

    @Named( "genderBooleanToString" )
    default String genderBooleanToString(Boolean gender) {
        return gender ? "male" : "female";
    }
}
