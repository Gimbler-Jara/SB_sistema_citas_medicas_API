package com.cibertec.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FirebaseStorageService {


    private final String bucketName = "digital-world-8d4cc.appspot.com";

    public record FirebaseUploadResult(String url, String path) {}

    public FirebaseUploadResult uploadFile(MultipartFile file, Integer idMedico) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío o no fue enviado.");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String fixedFilename = "firma" + extension; 

        // Ruta completa
        String fullPath = "firmaMedico-springboot/" + idMedico + "/" + fixedFilename;

        Bucket bucket = StorageClient.getInstance().bucket(bucketName);
        Blob blob = bucket.create(fullPath, file.getBytes(), file.getContentType());

        String url = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucketName,
                URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8));

        return new FirebaseUploadResult(url, fullPath);
    }


    public void deleteFile(String path) {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.get(path);
        if (blob != null) {
            blob.delete();
            System.out.println("Archivo eliminado: " + path);
        } else {
            System.out.println("No se encontró el archivo en Firebase Storage: " + path);
        }
    }
    
    public String getPublicUrl(String path) {
        try {
            return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucketName,
                URLEncoder.encode(path, StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al generar la URL de descarga pública desde Firebase", e);
        }
    }
    
    public String getBucketName() {
    	return bucketName;
    }


}
