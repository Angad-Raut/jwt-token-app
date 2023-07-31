package com.projectx.jwt.mysqldb.service;

import com.projectx.jwt.mysqldb.entity.Users;
import com.projectx.jwt.common.exceptions.EmailAlreadyExistException;
import com.projectx.jwt.common.exceptions.InvalidDataException;
import com.projectx.jwt.common.exceptions.MobileAlreadyExistException;
import com.projectx.jwt.common.exceptions.ResourceNotFoundException;
import com.projectx.jwt.payload.EntityIdDto;
import com.projectx.jwt.payload.UserDto;
import com.projectx.jwt.payload.ViewListDto;

import java.util.List;

public interface UserService {
    void addDefaultUser(UserDto userDto);
    String insertOrUpdate(UserDto userDto)throws InvalidDataException, MobileAlreadyExistException, EmailAlreadyExistException;
    Users getById(EntityIdDto dto)throws ResourceNotFoundException;
    List<ViewListDto> getAllUsers();
    Users getUserByUserName(String userName);
}
