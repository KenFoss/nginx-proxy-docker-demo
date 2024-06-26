package com.auth.demo.web.rest;

import com.auth.demo.jpa.dto.GoogleUserDTO;
import com.auth.demo.jpa.model.GoogleUser;
import com.auth.demo.jpa.repository.GoogleUserRepository;
import com.auth.demo.jpa.service.GoogleUserService;
import com.auth.demo.service.JwtGeneratorService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.auth.demo.config.GoogleConfig;
import org.springframework.web.client.RestTemplate;

@RestController
public class OAuth2Controller {

    private final GoogleConfig googleConfig;
    private final GoogleUserService googleUserService;
    private final GoogleUserRepository googleUserRepository;
    private final JwtGeneratorService jwtGeneratorService;

    public OAuth2Controller (GoogleConfig googleConfig,
                             GoogleUserService googleUserService,
                             GoogleUserRepository googleUserRepository,
                             JwtGeneratorService jwtGeneratorService) {
        this.googleConfig = googleConfig;
        this.googleUserService = googleUserService;
        this.googleUserRepository = googleUserRepository;
        this.jwtGeneratorService = jwtGeneratorService;
    }

    @CrossOrigin(origins={"http://localhost:3000", "http://localhost:8080"}, allowCredentials ="true")
    @PostMapping("/api/oauth2/google")
    public ResponseEntity<Object> oauth2Callback(@RequestHeader(name = "Authorization") String authorizationHeader) {

        try {
            // Extract the access token from the Authorization header
            String accessToken = authorizationHeader.replace("Bearer ", "");

            // Verify the access token using the Google ID Token Verifier
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleConfig.getClientId()))
                    .build();

            GoogleIdToken idToken = verifier.verify(accessToken);

            if (verifier.verify(accessToken) != null) {
                System.out.println("verified token");
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Get the user's Google ID and email
                String userId = payload.getSubject();
                String email = payload.getEmail();

                // if user does not exist create

                Optional<GoogleUser> existingUser = googleUserRepository.findByGoogleId(userId);
                GoogleUser validUser = existingUser.orElse(null);

                if (!existingUser.isPresent()) {
                    System.out.println("Registering new user.");
                    GoogleUserDTO newUser = new GoogleUserDTO();
                    newUser.setGoogleId(userId);
                    newUser.setGoogleEmail(email);

                    validUser = googleUserService.save(newUser);
                }

                // Generate a JWT to represent this user in our application.
                String newToken = jwtGeneratorService.generateToken(userId);

                // Create a response map with the user's information
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("userEmail", validUser.getEmail());
                responseMap.put("jwToken", newToken);
                return ResponseEntity.ok(responseMap);

            } else {
                return ResponseEntity.badRequest().body("Invalid access token");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error verifying access token: " + e.getMessage());
        }
    }

    @CrossOrigin(origins={"http://localhost:3000", "http://localhost:8080"}, allowCredentials ="true")
    @GetMapping("/api/oauth2/is-authenticated")
    public ResponseEntity<Object> getCsrf(){
        Map<String,Boolean> responseMap = new HashMap<>();
        responseMap.put("isAuthenticated", true);
        return ResponseEntity.ok(responseMap);
    }
}