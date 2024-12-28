package br.com.finance.finance.jwt;

import br.com.finance.finance.exception.NotFoundException;
import br.com.finance.finance.model.UserModel;
import br.com.finance.finance.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsServices implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(JwtUserDetailsServices.class);
    private UserRepository userRepository;
    private JwtUtils jwtUtils;

    public JwtUserDetailsServices(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> userDetails = userRepository.findByUsername(username);
        if(userDetails.isEmpty()) {
            throw new NotFoundException(String.format("User %s not found", username));
        }
        return new JwtUserDetails(userDetails.get());
    }

    public JwtToken getTokenAuthenticated(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User %s not found", username)));
        return jwtUtils.generateToken(user);
    }
}
