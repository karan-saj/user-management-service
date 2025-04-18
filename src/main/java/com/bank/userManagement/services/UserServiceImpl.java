package com.bank.userManagement.services;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.entity.UserEntity;
import com.bank.userManagement.excpetion.UserNotFoundException;
import com.bank.userManagement.repository.UserRepository;
import com.bank.userManagement.util.TransformationUtil;
import com.bank.userManagement.util.ValidationUtil;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;
    final private TransformationUtil util;
    final private ValidationUtil validation;

    public UserServiceImpl(UserRepository userRepository,
                           TransformationUtil util,
                           ValidationUtil validation) {
        this.userRepository = userRepository;
        this.util = util;
        this.validation = validation;
    }

    @Override
    public UserDTO fetchUserDetails(Long id) {
        UserEntity userDetails = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("Error: User Id is not present"));
        return util.convertUserDetails(userDetails);
    }

    @Override
    public UserDTO createUser(UserDTO userDetails) {
        validation.validateUserDetails(userDetails);

        UserEntity userEntity = userRepository.save(util.convertUserDetails(userDetails));
        return util.convertUserDetails(userEntity);
    }
}
