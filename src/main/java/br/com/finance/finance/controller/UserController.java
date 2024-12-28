package br.com.finance.finance.controller;

import br.com.finance.finance.dto.UserRecordDto;
import br.com.finance.finance.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @JsonView(UserRecordDto.UserView.UserRegisterPost.class)
    public ResponseEntity<String> registerUser(@RequestBody
                                               @Validated
                                               @JsonView(UserRecordDto.UserView.UserRegisterPost.class)
                                               UserRecordDto userRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRecordDto));
    }

    @PutMapping("/update")
    @JsonView(UserRecordDto.UserView.UserUpdatePut.class)
    public ResponseEntity<String> updateUser(@RequestBody
                                             @Validated
                                             @JsonView(UserRecordDto.UserView.UserUpdatePut.class)
                                             UserRecordDto userRecordDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRecordDto));
    }
}
