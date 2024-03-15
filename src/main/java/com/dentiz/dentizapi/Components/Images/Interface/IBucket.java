package com.dentiz.dentizapi.Components.Images.Interface;

import com.amazonaws.services.s3.model.Bucket;
import com.dentiz.dentizapi.Components.Images.DataSource.BucketObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IBucket {
    BucketObject uploadFile(MultipartFile multipartFile) throws IOException;
}
