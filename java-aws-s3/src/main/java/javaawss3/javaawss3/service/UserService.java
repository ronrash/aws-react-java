package javaawss3.javaawss3.service;

import java.util.List;
import java.util.UUID;

import javaawss3.javaawss3.model.UserProfile;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    List<UserProfile> getUserProfiles();

    public void saveAndUploadFileToAwsS3Buckets(UUID userProfileId, MultipartFile file);

    byte[] downloadFileFromAwsS3Buckets(UUID userProfileId);
}
