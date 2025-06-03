package com.example.learnrest.service;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Client s3Client;

    private String s3BucketName;

    public S3Service(String s3Region, String s3Endpoint, String s3AccessKey, String s3SecretKey, String s3BucketName) {
        AwsCredentials awsCreds = AwsBasicCredentials.create(s3AccessKey, s3SecretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(s3Region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        this.s3BucketName = s3BucketName;
    }

    public String uploadFile(MultipartFile file) throws Exception {

        // Extract the file extension
        String fileExtension = getFileExtension(file.getOriginalFilename());

        String filename = UUID.randomUUID().toString();

        // Append the extension to the UUID
        String finalFilename = filename + fileExtension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(this.s3BucketName)
                .key(finalFilename)
                .acl("private")
                .contentType(file.getContentType())
                .build();

        try (InputStream is = file.getInputStream()) {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(is, file.getSize()));
        }

        return "https://" + this.s3BucketName + ".s3.amazonaws.com/" + finalFilename;
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex); // e.g., .png, .jpg, .pdf
        }
        return ""; // No extension found
    }
}
