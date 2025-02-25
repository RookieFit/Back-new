package com.rookiefit.rookiefit.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.InputStream

@Configuration
class FirebaseConfig {
    @Bean
    fun FirebaseApp(): FirebaseApp {
        val serviceAccount: InputStream = javaClass.classLoader.getResourceAsStream("firebase.json")
            ?: throw Exception("Could not load firebase config")
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setStorageBucket("rookiefit-edf53")
            .build()
        return if(FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }else{
            FirebaseApp.getInstance()
        }
    }
    @Bean
    fun storageClient(firebaseApp: FirebaseApp): StorageClient {
        return StorageClient.getInstance(firebaseApp)
    }

    @Bean
    fun storage(): Storage {
        // 기본 Storage 인스턴스 생성
        return StorageOptions.getDefaultInstance().service
    }
}