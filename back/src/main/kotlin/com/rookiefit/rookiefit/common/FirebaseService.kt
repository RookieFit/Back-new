package com.rookiefit.rookiefit.common

import com.google.cloud.storage.Storage
import com.google.firebase.cloud.StorageClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import javax.imageio.ImageIO

@Service
class FirebaseService(
    private val storage: Storage,
    private val storageClient: StorageClient
) {
    private companion object {
        private val LOG = LoggerFactory.getLogger(FirebaseService::class.java)
    }
    fun uploadImageFile(file: MultipartFile?): String {
        val buckName = "rookiefit-edf53"
        val originalFileName = file?.originalFilename
        val fileName = "$originalFileName-${System.currentTimeMillis()}.jpg"

        val inputStream = file?.inputStream
        val image = ImageIO.read(inputStream)
        if (image == null) {
            LOG.error("이미지를 읽을 수 없습니다")
            throw Exception("Image not found")
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(image, "jpg", byteArrayOutputStream)
        val imageData = byteArrayOutputStream.toByteArray()
        if(imageData.isEmpty()) {
            throw Exception("올바른 이미지 형식이 아닙니다")
        }

        try {
            storageClient.bucket(buckName).create(fileName, imageData, "image/jpg")
        } catch (e: Exception) {
            LOG.error("파일 업로드 중 오류 발생: ${e.message}\", e")
            throw e
        }
        val encodedFileName = URLEncoder.encode(fileName, "UTF-8")
        val downloadUri = "https://firebasestorage.googleapis.com/v0/b/$buckName/o/$encodedFileName?alt=media"

        return downloadUri
    }
    fun uploadImageFiles(imageList: List<MultipartFile>): List<String> {
        return imageList.map { uploadImageFile(it) }
    }
}