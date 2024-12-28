package br.com.finance.finance.jwt;

import br.com.finance.finance.constants.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private JwtUserDetailsServices detailsServices;
    private JwtUtils jwtUtils;

    public JwtAuthorizationFilter() {}

    public JwtAuthorizationFilter(JwtUserDetailsServices detailsServices, JwtUtils jwtUtils) {
        this.detailsServices = detailsServices;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(Constants.JWT_AUTHORIZATION);

        if(token == null || token.isEmpty() || !token.startsWith(Constants.JWT_BEARER)) {
            log.info("JWT null or wasn't initialize.");
            filterChain.doFilter(request, response);
            return;
        }

        if(!jwtUtils.isValidToken(token)) {
            log.warn("Invalid token ou expired.");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtils.getUsernameFromToken(token);

        toAuthentication(request, username);

        filterChain.doFilter(request, response);
    }
    private void toAuthentication(HttpServletRequest request, String username) {
        UserDetails userDetails = detailsServices.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
