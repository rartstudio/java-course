package com.example.learnrest.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.learnrest.dto.request.user.CreateProfileForm;
import com.example.learnrest.entity.User;
import com.example.learnrest.entity.UserProfile;
import com.example.learnrest.exception.ImageUploadException;
import com.example.learnrest.repository.UserProfileRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final S3Service s3Service;

    public UserProfileService(UserProfileRepository userProfileRepository, S3Service s3Service) {
        this.userProfileRepository = userProfileRepository;
        this.s3Service = s3Service;
    }

    public UserProfile createProfile(CreateProfileForm form, User user) {
        MultipartFile image = form.getImage();

        String imageUrl;
        try {
            imageUrl = s3Service.uploadFile(image);
        } catch (Exception e) {
            throw new ImageUploadException("Failed to upload image to S3");
        }

        UserProfile profile = new UserProfile();
        profile.setDateOfBirth(form.getDateOfBirth());
        profile.setImage(imageUrl);
        profile.setUser(user);

        userProfileRepository.save(profile);

        UserProfile saved = userProfileRepository.save(profile);

        return saved;
    }

    public Optional<UserProfile> getProfile(User user) {
        return userProfileRepository.findByUser(user);
    }
}
