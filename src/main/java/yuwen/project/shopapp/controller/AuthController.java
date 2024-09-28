package yuwen.project.shopapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuwen.project.shopapp.dto.SignInRequest;
import yuwen.project.shopapp.dto.SignUpRequest;
import yuwen.project.shopapp.exceptions.InvalidCredentialsException;
import yuwen.project.shopapp.service.AuthenticationService;
import yuwen.project.shopapp.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Validated SignUpRequest signUpRequest) {
        try {
            userService.registerUser(signUpRequest);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle different types of exceptions accordingly
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Validated SignInRequest signInRequest) {

        try {
            // Delegate the authentication process to the AuthenticationService
            String jwt = authenticationService.authenticate(
                    signInRequest.getUsername(),
                    signInRequest.getPassword()
            );
            // If authentication is successful, return the JWT token

            return ResponseEntity.ok(jwt);
        } catch (BadCredentialsException ex) {
            // If authentication fails, throw a custom exception

            throw new InvalidCredentialsException("Incorrect credentials, please try again.");
        }
    }


}

