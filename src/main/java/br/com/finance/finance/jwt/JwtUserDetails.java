package br.com.finance.finance.jwt;

import br.com.finance.finance.model.UserModel;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.UUID;

public class JwtUserDetails extends User {
    UserModel user;

    public JwtUserDetails(UserModel userModel) {
        super(userModel.getUsername(),
                userModel.getPassword(),
                AuthorityUtils.createAuthorityList(userModel.getRole().name()));
        this.user = userModel;
    }

    public UUID getId() {
        return this.user.getId();
    }

    public String getRole() {
        return this.user.getRole().name();
    }
}
