package javaawss3.javaawss3.serviceimpl;

import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javaawss3.javaawss3.common.BucketName;
import javaawss3.javaawss3.filedatastore.FakeFileDataStore;
import javaawss3.javaawss3.model.UserProfile;
import javaawss3.javaawss3.service.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final FakeFileDataStore fakeFileDataStore;

    public UserServiceImpl(final FakeFileDataStore fakeFileDataStore) {
        this.fakeFileDataStore = fakeFileDataStore;
    }

    @Override
    public List<UserProfile> getUserProfiles() {
        return fakeFileDataStore.getUserProfiles();
    }

    @Override
    public void saveAndUploadFileToAwsS3Buckets(final UUID userProfileId, final MultipartFile file) {

        isFileEmpty(file);

        final UserProfile userProfile = getUserProfileOrThrowException(userProfileId);
        // grab meatdata from the file and update the userProfile
        var metadata = extractMetadata(file);
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), userProfileId);
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try {
            fakeFileDataStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            userProfile.setAmazonS3link(fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] downloadFileFromAwsS3Buckets(final UUID userProfileId) {
        // to get to the path I need the bucketName + folder path
        final UserProfile userProfile = getUserProfileOrThrowException(userProfileId);
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), userProfile.getUserProfileId());
        return userProfile.getAmazonS3link()
                .map(link -> fakeFileDataStore.download(path, link)).orElse(new byte[]{0});
    }

    private void isFileAnImageType(final MultipartFile file) {
        if (!Arrays.asList(IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType(), IMAGE_JPEG.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("The Fileshould be an Image");
        }
    }

    private Map<String, String> extractMetadata(final MultipartFile file) {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", file.getContentType());
        map.put("Content-Length", String.valueOf(file.getSize()));
        return map;
    }

    private UserProfile getUserProfileOrThrowException(final UUID userProfileId) {
        return fakeFileDataStore
                .getUserProfiles()
                .stream().
                filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst().orElseThrow(() -> new RuntimeException(String.format("UserProfile %s did not match", userProfileId)));

    }

    private void isFileEmpty(final MultipartFile file) {
        if (Objects.isNull(file)) {
            throw new IllegalStateException("Cannot upload an empty file ");
        }
    }
}
