package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.config.FirebaseConfiguration;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseService {

    @Autowired
    FirebaseConfiguration firebaseConfiguration;

    public String uploadImage(MultipartFile file) throws IOException {
        String storageBucket = "birthday-party-for-kids-243a5.appspot.com";
        Storage storage = StorageClient.getInstance().bucket().getStorage();
        String fileName = "image/" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(storageBucket, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        String downloadUrl = blob.getMediaLink();
        String signeUrl = blob.signUrl(730 * 3, TimeUnit.HOURS).toString();

        return signeUrl;

    }
}
