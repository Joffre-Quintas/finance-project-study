package br.com.finance.finance.dto;

import br.com.finance.finance.enums.Role;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecordDto(
        @NotBlank(groups = {UserView.UserRegisterPost.class, UserView.UserLoginPost.class, UserView.UserUpdatePut.class})
        @Size(min = 6, max = 20)
        @JsonView({UserView.UserRegisterPost.class, UserView.UserLoginPost.class, UserView.UserUpdatePut.class})
        String username,

        @NotBlank(groups = {UserView.UserRegisterPost.class, UserView.UserLoginPost.class, UserView.UserUpdatePut.class})
        @Size(min = 6, max = 12)
        @JsonView({UserView.UserRegisterPost.class, UserView.UserLoginPost.class, UserView.UserUpdatePut.class})
        String password,

        @NotBlank(groups = {UserView.UserRegisterPost.class, UserView.UserUpdatePut.class})
        @Size(min = 6, max = 12)
        @JsonView({UserView.UserRegisterPost.class, UserView.UserUpdatePut.class})
        String confirmPassword,

        @NotBlank(groups = {UserView.UserUpdatePut.class})
        @Size(min = 6, max = 12)
        @JsonView({UserView.UserUpdatePut.class})
        String newPassword,

        @NotBlank(groups = {UserView.UserRegisterPost.class})
        @Email
        @JsonView(UserView.UserRegisterPost.class)
        String email,

        Role role
) {
    public interface UserView {
        interface UserRegisterPost{}
        interface UserLoginPost{}
        interface UserUpdatePut{}
    }
}
