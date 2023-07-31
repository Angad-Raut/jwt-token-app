package com.projectx.jwt.mysqldb.service;

import com.projectx.jwt.mysqldb.entity.Users;
import com.projectx.jwt.common.exceptions.EmailAlreadyExistException;
import com.projectx.jwt.common.exceptions.InvalidDataException;
import com.projectx.jwt.common.exceptions.MobileAlreadyExistException;
import com.projectx.jwt.common.exceptions.ResourceNotFoundException;
import com.projectx.jwt.payload.EntityIdDto;
import com.projectx.jwt.payload.UserDto;
import com.projectx.jwt.payload.ViewListDto;
import com.projectx.jwt.mysqldb.repository.UsersRepository;
import com.projectx.jwt.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void addDefaultUser(UserDto userDto) {
        Users users = usersRepository.findUsersByMobile(userDto.getUserMobile());
        if (users==null) {
            Users usersDetails = Users.builder()
                    .userName(userDto.getUserName())
                    .userEmail(userDto.getUserEmail())
                    .userMobile(userDto.getUserMobile())
                    .userStatus(true)
                    .roles(userDto.getRoles())
                    .userPassword(passwordEncoder.encode(userDto.getUserPassword()))
                    .build();
            usersRepository.save(usersDetails);
        }
    }

    @Transactional
    @Override
    public String insertOrUpdate(UserDto userDto)throws InvalidDataException,
            MobileAlreadyExistException, EmailAlreadyExistException {
        Users users = null;
        String result = null;
        if (userDto.getUserId()==null) {
                 isMobileExist(userDto.getUserMobile());
                 isEmailExist(userDto.getUserEmail());
                 users = Users.builder()
                         .userName(userDto.getUserName())
                         .userEmail(userDto.getUserEmail())
                         .userMobile(userDto.getUserMobile())
                         .userPassword(passwordEncoder.encode(userDto.getUserPassword()))
                         .userStatus(true)
                         .roles(userDto.getRoles())
                         .build();
                 result = Constants.ADD_USER;
        } else {
                 users = getById(new EntityIdDto(userDto.getUserId()));
                 if (!userDto.getUserName().equals(users.getUserName())) {
                     users.setUserName(userDto.getUserName());
                 }
                 if (!userDto.getUserEmail().equals(users.getUserEmail())) {
                     isEmailExist(userDto.getUserEmail());
                     users.setUserEmail(userDto.getUserEmail());
                 }
                 if (!userDto.getUserMobile().equals(users.getUserMobile())) {
                     isMobileExist(userDto.getUserMobile());
                     users.setUserMobile(userDto.getUserMobile());
                 }
                 if (!userDto.getRoles().equals(users.getRoles())) {
                     users.getRoles().clear();
                     users.setRoles(userDto.getRoles());
                 }
                 result = Constants.UPDATE_USER;
        }
        try {
             usersRepository.save(users);
             return result;
        } catch (MobileAlreadyExistException e) {
            throw new MobileAlreadyExistException(e.getMessage());
        } catch (EmailAlreadyExistException e) {
            throw new EmailAlreadyExistException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public Users getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            Users users = usersRepository.findByUserId(dto.getEntityId());
            return users;
        } catch (Exception e) {
            throw new ResourceNotFoundException(Constants.USER_DETAILS_NOT_EXIST);
        }
    }

    @Override
    public List<ViewListDto> getAllUsers() {
        try {
            AtomicInteger index = new AtomicInteger(0);
            List<Users> list = usersRepository.findAll();
            return list.stream()
                    .map(data -> ViewListDto.builder()
                            .srNo(index.incrementAndGet())
                            .userId(data.getId())
                            .userName(data.getUserName())
                            .userEmail(data.getUserEmail())
                            .userMobile(data.getUserMobile())
                            .userRoles(data.getUserRole())
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public Users getUserByUserName(String userName) {
        Users users = null;
        if (Constants.isMobile(userName)){
            users = usersRepository.findUsersByMobile(Long.parseLong(userName));
        } else {
            users = usersRepository.findUsersByEmail(userName);
        }
        return users;
    }

    private Boolean isMobileExist(Long mobile) {
        if (usersRepository.existsByUserMobile(mobile)) {
            throw new MobileAlreadyExistException(Constants.MOBILE_NUMBER_ALREADY_EXIST);
        } else {
            return false;
        }
    }
    private Boolean isEmailExist(String email) {
        if (usersRepository.existsByUserEmail(email)) {
            throw new EmailAlreadyExistException(Constants.EMAIL_ID_ALREADY_EXIST);
        } else {
            return false;
        }
    }
}
