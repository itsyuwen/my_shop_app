package yuwen.project.shopapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import yuwen.project.shopapp.exceptions.InvalidCredentialsException;
import yuwen.project.shopapp.security.JwtTokenProvider;
import yuwen.project.shopapp.repository.UserRepository;



@Service
public class AuthenticationService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder,
                                 UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    public String authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return jwtTokenProvider.generateToken(authentication);
        } catch (AuthenticationException e) {

            throw new InvalidCredentialsException("Incorrect credentials, please try again.");
        }
    }


}


