package javaawss3.javaawss3.model;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    private  UUID userProfileId;
    private  String userName;
    private String amazonS3link; // link to the s3

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UserProfile that = (UserProfile) o;
        return Objects.equals(userProfileId, that.userProfileId)
                && Objects.equals(userName, that.userName)
                && Objects.equals(amazonS3link, that.amazonS3link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfileId, userName, amazonS3link);
    }

    public Optional<String> getAmazonS3link() {
        return Optional.ofNullable(amazonS3link);
    }

    public void setAmazonS3link(final String amazonS3link) {
        this.amazonS3link = amazonS3link;
    }
}
