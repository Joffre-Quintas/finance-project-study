package br.com.finance.finance.service.impl;

import br.com.finance.finance.dto.UserRecordDto;
import br.com.finance.finance.enums.Role;
import br.com.finance.finance.exception.ConflictArgumentException;
import br.com.finance.finance.exception.NotFoundException;
import br.com.finance.finance.model.UserModel;
import br.com.finance.finance.repository.UserRepository;
import br.com.finance.finance.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String registerUser(UserRecordDto userRecordDto) {
        if (!userRecordDto.password().equals(userRecordDto.confirmPassword())) {
            throw new ConflictArgumentException("Passwords didn't match.");
        }

        UserModel newUser = new UserModel();
        BeanUtils.copyProperties(userRecordDto, newUser);

        newUser.setPassword(passwordEncoder.encode(userRecordDto.password()));
        newUser.setRole(Role.USER);
        newUser.setCreatedAt(new Date().toInstant());

        userRepository.save(newUser);

        return "Register successfully.";
    }

    @Override
    @Transactional
    public String updateUser(UserRecordDto userRecordDto) {
        UserModel user = userRepository.findByUsername(userRecordDto.username())
                .orElseThrow(() -> new NotFoundException(String.format("Username %s not found.", userRecordDto.username())));

        if(!passwordEncoder.matches(userRecordDto.password(), user.getPassword())) {
            throw new ConflictArgumentException("Passwords didn't match.");
        }
        if(!userRecordDto.confirmPassword().equals(userRecordDto.newPassword())) {
            throw new ConflictArgumentException("New password and confirm password are different");
        }

        user.setPassword(passwordEncoder.encode(userRecordDto.newPassword()));
        user.setUpdatedAt(new Date().toInstant());

        userRepository.save(user);

        return "Update successfully";
    }
}
