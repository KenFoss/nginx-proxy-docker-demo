package com.auth.demo.jpa.service;

import com.auth.demo.jpa.dto.GoogleUserDTO;
import com.auth.demo.jpa.model.GoogleUser;
import com.auth.demo.jpa.repository.GoogleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleUserService {

    @Autowired
    private GoogleUserRepository googleUserRepository;

    // Methods for interacting with the repository
    // e.g., save, update, delete, find, etc.
    public GoogleUser save(GoogleUserDTO googleUserDTO) {
        GoogleUser googleUser = new GoogleUser();
        googleUser.setEmail(googleUserDTO.getGoogleEmail());
        googleUser.setGoogleId(googleUserDTO.getGoogleId());
        return googleUserRepository.save(googleUser);
    }
}