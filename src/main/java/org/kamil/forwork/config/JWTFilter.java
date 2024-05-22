package org.kamil.forwork.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.kamil.forwork.services.UsersDetailsService;
import org.kamil.forwork.util.JWTUtil;
import org.kamil.forwork.util.exceptions.user.UserNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JWTFilter extends OncePerRequestFilter {

    private HandlerExceptionResolver handlerExceptionResolver;
    private final UsersDetailsService usersDetailsService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authentication");
        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(!token.isBlank()){
                try {
                    String username = jwtUtil.validateTokenAndRetrievedClaim(token);
                    UserDetails userDetails = usersDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException | UserNotFoundException e) {
                    handlerExceptionResolver.resolveException(request, response, null, e);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
