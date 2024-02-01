package com.management.management.mapper;

import com.management.management.domain.user.User;
import com.management.management.dtos.user.UserInfoDTO;

public class UserMapper {

    public UserInfoDTO userInfoDTO(User user){
        return new UserInfoDTO(user.getId(), user.getName(), user.getSurname(), user.getUsername(), user.getEmail());
    }
}
