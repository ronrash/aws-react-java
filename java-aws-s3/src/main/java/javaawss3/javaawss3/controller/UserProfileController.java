package javaawss3.javaawss3.controller;

import java.util.List;
import java.util.UUID;

import javaawss3.javaawss3.model.UserProfile;
import javaawss3.javaawss3.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
@Slf4j
public class UserProfileController {
    private final UserService userService;

    public UserProfileController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserProfile>> getUserProfiles() {
        return new ResponseEntity<>(userService.getUserProfiles(), HttpStatus.OK);
    }

    // here we are getting a file from the ui and saving it in the aws s2 bucket
    @PostMapping(path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveAndUploadFileToAwsS3Buckets(@PathVariable("userProfileId") final UUID userProfileId, @RequestParam("file") MultipartFile file) {
        log.info(" >> UserProfileController :: saveAndUploadFileToAwsS3Buckets : {}",userProfileId);
        userService.saveAndUploadFileToAwsS3Buckets(userProfileId, file);
            return new ResponseEntity(HttpStatus.CREATED);
    }

    // here we are getting a file from the aws
    @GetMapping(path = "{userProfileId}/image/download")
    public ResponseEntity<byte[]> downloadFileFromAwsS3Buckets(@PathVariable("userProfileId")final UUID userProfileId){
        log.info(" >> UserProfileController :: downloadFileFromAwsS3Buckets : {}",userProfileId);
       ;
        return new ResponseEntity( userService.downloadFileFromAwsS3Buckets(userProfileId),HttpStatus.OK);
    }
}
