package javaawss3.javaawss3.filedatastore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import javaawss3.javaawss3.model.UserProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FakeFileDataStore {


    private final AmazonS3 amazonS3;
    @Autowired
    public FakeFileDataStore(final AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("5358f7a0-ea43-4787-a55b-0809c4986a8c"), "jack", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("5c737c31-5afe-41e1-b7b8-91694da9efec"), "jill", null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }

    public void save(String path,
                     String fileName,
                     Optional<Map<String, String>> optionalMetaData,
                     InputStream inputStream) {
//
//         public PutObjectRequest(String bucketName, String key, InputStream input, ObjectMetadata metadata) {
//             super(bucketName, key, input, metadata);
//         }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
//          map.forEach((K,V)->objectMetadata.addUserMetadata(K,V)); both are same way to do
                map.forEach(objectMetadata::addUserMetadata);
            }
        });

        try {
            amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException amazonServiceException) {
            throw new IllegalStateException("Failed to store the file ", amazonServiceException);
        }

    }

    public byte[] download(final String path, final String key) {
        try{
            final S3Object object = amazonS3.getObject(path, key);
            final S3ObjectInputStream objectContent = object.getObjectContent();
            return IOUtils.toByteArray(objectContent);
        }catch (AmazonServiceException | IOException exception ){
            throw new IllegalStateException("Failed to retrieve the file ", exception);
        }
    }
}
