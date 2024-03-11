package com.bookingBirthday.bookingbirthdayforkids.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfiguration {

    @Value("classpath:birthday-party-for-kids-243a5-firebase-adminsdk-rc3cl-ec5faa7477.json")
    Resource resourceFile;

    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @PostConstruct
    public void initializeFirebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount = resourceFile.getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resourceFile.getInputStream()))
                    .setServiceAccountId("firebase-adminsdk-rc3cl@birthday-party-for-kids-243a5.iam.gserviceaccount.com")
                    .setStorageBucket("birthday-party-for-kids-243a5.appspot.com")
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }
}
