package kpu.cybersecurity.training.service;

import kpu.cybersecurity.training.config.EnvProperties;
import kpu.cybersecurity.training.domain.dto.response.ResUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.file.Path;

@Service
public class AWSService {
    private final EnvProperties envProperties;

    private S3Client s3Client;

    @Autowired
    public AWSService(EnvProperties envProperties) {
        this.envProperties = envProperties;

        String accessKey = this.envProperties.getAws().getAccessKey();
        String secretKey = this.envProperties.getAws().getSecretKey();

        s3Client = S3Client.builder()
                .region(Region.of(this.envProperties.getAws().getRegion()))
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public ResUploadDTO uploadFile(MultipartFile file, String prefix) throws IOException {
        try {
            String bucketName = envProperties.getAws().getS3().getBucket();
            String key = prefix + file.getOriginalFilename();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            try (InputStream inputStream = file.getInputStream()) {
                PutObjectResponse response = s3Client.putObject(request, RequestBody.fromInputStream(inputStream, file.getSize()));

                String url = s3Client.utilities().getUrl(r -> r.bucket(bucketName).key(key)).toString();
                String contentType = file.getContentType();

                ResUploadDTO dto = new ResUploadDTO(contentType, key, url);
                return dto;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InterruptedIOException("File Upload Failed.");
        }
    }
}
