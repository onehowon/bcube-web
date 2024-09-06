package com.ebiz.bcube.global.application;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;

import java.io.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OracleObjectStorageService {

    private final ObjectStorageClient objectStorageClient;
    private final String namespace;
    private final String bucketName;
    private final String region;

    public OracleObjectStorageService(
            @Value("${oracle.cloud.namespace}") String namespace,
            @Value("${oracle.cloud.bucket-name}") String bucketName,
            @Value("${oracle.cloud.private-key-file-path}") String privateKeyFilePath,
            @Value("${oracle.cloud.region}") String region,
            @Value("${oracle.cloud.tenancy-id}") String tenancyId,
            @Value("${oracle.cloud.user-id}") String userId,
            @Value("${oracle.cloud.fingerprint}") String fingerprint) throws Exception {

        File privateKeyFile = new File(privateKeyFilePath);

        if (!privateKeyFile.exists()) {
            throw new FileNotFoundException("Private key 파일을 찾을 수 없습니다: " + privateKeyFilePath);
        }

        try (InputStream privateKeyStream = new FileInputStream(privateKeyFile)) {
            SimpleAuthenticationDetailsProvider provider = SimpleAuthenticationDetailsProvider.builder()
                    .tenantId(tenancyId)
                    .userId(userId)
                    .fingerprint(fingerprint)
                    .privateKeySupplier(() -> {
                        try {
                            return new FileInputStream(privateKeyFilePath);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException("Private key 파일을 찾을 수 없습니다: " + privateKeyFilePath, e);
                        }
                    })
                    .build();

            System.out.println("ObjectStorageClient created successfully.");
            this.objectStorageClient = new ObjectStorageClient(provider);
            this.objectStorageClient.setRegion(region);

            this.namespace = namespace;
            this.bucketName = bucketName;
            this.region = region;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 접근 오류: " + e.getMessage(), e);
        }
    }

    public String uploadObject(String objectName, byte[] content) {
        System.out.println("Uploading object...");
        System.out.println("Namespace: " + namespace);
        System.out.println("Region: " + region);
        System.out.println("Bucket Name: " + bucketName);
        System.out.println("Object Name: " + objectName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .namespaceName(namespace)
                .bucketName(bucketName)
                .objectName(objectName)
                .putObjectBody(new ByteArrayInputStream(content))
                .build();

        System.out.println("PutObjectRequest created with:");
        System.out.println("Namespace Name: " + putObjectRequest.getNamespaceName());
        System.out.println("Bucket Name: " + putObjectRequest.getBucketName());
        System.out.println("Object Name: " + putObjectRequest.getObjectName());

        try {
            PutObjectResponse putObjectResponse = objectStorageClient.putObject(putObjectRequest);
            String objectUrl = String.format(
                    "https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                    region, namespace, bucketName, objectName
            );

            System.out.println("오브젝트가 업로드 되었습니다 ETag: " + putObjectResponse.getETag());
            System.out.println("오브젝트 URL: " + objectUrl);

            return objectUrl;
        } catch (Exception e) {
            System.err.println("Error uploading object: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getRegion() {
        return region;
    }
}
