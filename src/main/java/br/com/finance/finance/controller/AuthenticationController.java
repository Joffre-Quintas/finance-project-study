package br.com.finance.finance.controller;

import br.com.finance.finance.dto.UserRecordDto;
import br.com.finance.finance.jwt.JwtToken;
import br.com.finance.finance.jwt.JwtUserDetailsServices;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtUserDetailsServices jwtUserDetailsServices;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(JwtUserDetailsServices jwtUserDetailsServices,
                                    AuthenticationManager authenticationManager) {
        this.jwtUserDetailsServices = jwtUserDetailsServices;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    @JsonView(UserRecordDto.UserView.UserLoginPost.class)
    public ResponseEntity<String> login(@RequestBody
                                          @Validated
                                          UserRecordDto userRecordDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userRecordDto.username(), userRecordDto.password());
        authenticationManager.authenticate(authenticationToken);
        JwtToken token = jwtUserDetailsServices.getTokenAuthenticated(userRecordDto.username());

        return ResponseEntity.status(HttpStatus.OK).body(token.token());
    }
}
