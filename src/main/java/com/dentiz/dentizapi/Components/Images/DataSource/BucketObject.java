package com.dentiz.dentizapi.Components.Images.DataSource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BucketObject {
    private String fileName;
    private String bucketName;

    public BucketObject(String fileName, String bucketName) {
        this.fileName = fileName;
        this.bucketName = bucketName;
    }
}
