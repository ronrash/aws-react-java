package javaawss3.javaawss3.common;

public enum BucketName {

    PROFILE_IMAGE("rohit-spring-boot-upload");

    private final String bucketName;

    BucketName(final String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}