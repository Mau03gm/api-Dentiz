package com.dentiz.dentizapi.Components.Images.DataSource;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.dentiz.dentizapi.Components.Images.Interface.IBucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Configuration
@EnableConfigurationProperties
public class S3DataSource implements IBucket {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${aws.access.key}")
    private  String accessKey;

    @Value("${aws.secret.key}")
    private  String secretKey;

    S3Client s3Client;
    public S3DataSource() {
        this.s3Client = new S3Client();
    }

    @Override
    public BucketObject uploadFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        s3Client.getClientAWS(accessKey, secretKey)
                .putObject(
                        bucketName,
                        fileName,
                        convertMultiPartFileToFile(multipartFile));
        return new BucketObject(fileName,bucketName);

    }

    private static File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(convFile);
        outputStream.write(file.getBytes());
        outputStream.close();
        return convFile;
    }
}
