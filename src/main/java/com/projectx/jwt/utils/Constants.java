package com.projectx.jwt.utils;

import com.projectx.jwt.payload.UserDto;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Constants {


    public static final Integer refreshTokenExpirationDays = 30;
    public static final Integer accessTokenExpirationMinutes = 5;

    public static final long accessTokenExpirationMs = (long) accessTokenExpirationMinutes * 60 * 1000;
    public static final long refreshTokenExpirationMs = (long) refreshTokenExpirationDays * 24 * 60 * 60 * 1000;

    public static final Long MOBILE=9766945760L;
    public static final String NAME="Angad Raut";
    public static final String EMAIL="angadraut75@gmail.com";
    public static final String PASSWORD="angad@75";
    public static final List<String> ROLES=Arrays.asList("Admin");
    public static final String ADD_USER="User inserted successfully!!";
    public static final String UPDATE_USER="User details updated successfully!!";
    public static final String MOBILE_NUMBER_ALREADY_EXIST="Mobile number already exist in the system!!";
    public static final String EMAIL_ID_ALREADY_EXIST="Email id already exist in the system!!";
    public static final String USER_DETAILS_NOT_EXIST="User details not present in the system!!";
    public static final String REFRESH_TOKEN_EXPIRED="Refresh token was expired, please login in again!!";

    public static UserDto setAdminData(){
        return UserDto.builder()
                .userName(NAME)
                .userEmail(EMAIL)
                .userMobile(MOBILE)
                .userPassword(PASSWORD)
                .roles(ROLES)
                .build();

    }

    public static Boolean isMobile(String input) {
        return input.chars().allMatch(Character::isDigit);
    }
}
