package com.rookiefit.rookiefit.common

import com.google.cloud.storage.Storage
import com.google.firebase.cloud.StorageClient
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
    fun uploadImageFile(file: MultipartFile): String {
        val buckName = "rookiefit-edf53"
        val originalFileName = file.originalFilename
        val fileName = "$originalFileName-${System.currentTimeMillis()}.jpg"

        val inputStream = file.inputStream
        val image = ImageIO.read(inputStream)
        if (image == null) {
            println("이미지 파일이 손상되었거나 유효하지 않습니다.")
            throw Exception("Image not found")
        }
        println("이미지 읽기 성공")

        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(image, "jpg", byteArrayOutputStream)
        val imageData = byteArrayOutputStream.toByteArray()
        if(imageData.isEmpty()) {
            throw Exception("올바른 이미지 형식이 아닙니다")
        }

        try {
            storageClient.bucket(buckName).create(fileName, imageData, "image/jpg")
        } catch (e: Exception) {
            println("파일 업로드 중 오류 발생: ${e.message}")
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