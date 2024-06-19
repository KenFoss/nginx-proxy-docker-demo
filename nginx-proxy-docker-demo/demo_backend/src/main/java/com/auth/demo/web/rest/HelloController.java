package com.auth.demo.web.rest;


import com.auth.demo.jpa.model.GoogleUser;
import com.auth.demo.jpa.repository.GoogleUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class HelloController {

    private final GoogleUserRepository googleUserRepository;

    public HelloController(GoogleUserRepository googleUserRepository) {
        this.googleUserRepository = googleUserRepository;
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> getHelloMessage(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();

        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Get the UserDetails object from the authentication object
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Assuming the UserDetails object has a method to get the user's email
            String userId = userDetails.getUsername();

            Optional<GoogleUser> googleUser = googleUserRepository.findByGoogleId(userId);

            if (googleUser.isPresent()) {
                String userEmail = googleUser.get().getEmail();
                response.put("message", "hello " + userEmail);
                // Return the response entity with the user's email
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            // Return an if
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}