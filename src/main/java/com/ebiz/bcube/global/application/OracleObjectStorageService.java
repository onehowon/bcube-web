package com.ebiz.bcube.global.application;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

        InputStream privateKeyStream = getClass().getClassLoader().getResourceAsStream(privateKeyFilePath);
        if (privateKeyStream == null) {
            throw new IOException("Private key를 찾을 수 없습니다.: " + privateKeyFilePath);
        }

        SimpleAuthenticationDetailsProvider provider = SimpleAuthenticationDetailsProvider.builder()
                .tenantId(tenancyId)
                .userId(userId)
                .fingerprint(fingerprint)
                .privateKeySupplier(() -> privateKeyStream)
                .build();

        this.objectStorageClient = new ObjectStorageClient(provider);
        this.namespace = namespace;
        this.bucketName = bucketName;
        this.region = region;
    }

    public void uploadObject(String objectName, byte[] content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .namespaceName(namespace)
                .bucketName(bucketName)
                .objectName(objectName)
                .putObjectBody(new ByteArrayInputStream(content))
                .build();

        PutObjectResponse putObjectResponse = objectStorageClient.putObject(putObjectRequest);
        System.out.println("오브젝트가 업로드 되었습니다 ETag: " + putObjectResponse.getETag());
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