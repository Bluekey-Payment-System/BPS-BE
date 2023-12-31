package com.github.bluekey.s3.manager;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.github.bluekey.s3.config.S3Configuration;
import java.io.IOException;

import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class AwsS3Manager implements ResourceManager {
	@Value("${cloud.aws.s3.excel-bucket}")
	private String excelBucket;
	@Value("${cloud.aws.s3.image-bucket}")
	private String imageBucket;
	@Value("${cloud.aws.region.static}")
	private String region;

	private final AmazonS3Client amazonS3Client;

	/**
	 * s3에 파일을 업로드 합니다.
	 *
	 * @param multipartFile 업로드할 파일
	 * @param key s3에 저장될 파일의 이름 혹은 경로 포함 이름
	 */
	public String upload(MultipartFile multipartFile, String key, S3PrefixType type) {
		log.info("uploading file to s3: {}", type.getValue() + key);
		ObjectMetadata objectMetadata = createObjectMetadata(multipartFile);
		String bucket = getBucketName(type);
		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, type.getValue() + key, multipartFile.getInputStream(), objectMetadata);
			amazonS3Client.putObject(putObjectRequest);
		}
		catch (AmazonS3Exception | IOException e) {
			// TODO: amazonS3에 대한 exception 처리라서 따로 에러 메세지 처리를 안해줬으나, 다른 exception과 포맷을 통일
			log.error("s3 upload error: {}", e.getMessage());
			e.getStackTrace();
		}
		return "https://" + bucket +".s3." + region + ".amazonaws.com/" + type.getValue() + key;
	}

	public String getS3Key(String s3Url, S3PrefixType type) {
		return s3Url.split(type.getValue())[1];
	}

	/**
	 * s3에 저장된 파일을 삭제합니다.
	 *
	 * @param key 삭제할 파일의 이름 혹은 경로 포함 이름
	 */
	public void delete(String key, S3PrefixType type) {
		log.info("deleting file from s3: {}", key);
		String bucket = getBucketName(type);
		try {
			amazonS3Client.deleteObject(bucket, key);
		} catch (AmazonS3Exception e) {
			log.error("s3 delete error: {}", e.getMessage());
			e.getStackTrace();
		}
	}

	public S3Object getS3Value(String fileName, S3PrefixType type) {
		String bucket = getBucketName(type);
		return amazonS3Client.getObject(bucket, fileName);
	}

	/**
	 * s3에 업로드할 파일의 메타데이터를 생성합니다.
	 *
	 * @param multipartFile 업로드할 파일
	 * @return 업로드할 파일의 메타데이터
	 */
	// TODO: File size는 application.properties에서 관리
	private ObjectMetadata createObjectMetadata(MultipartFile multipartFile) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(multipartFile.getContentType());
		objectMetadata.setContentLength(multipartFile.getSize());
		return objectMetadata;
	}

	private String getBucketName(S3PrefixType type) {
		if (type == S3PrefixType.EXCEL) {
			return excelBucket;
		} else if (type == S3PrefixType.IMAGE)
			return imageBucket;
		return null;
	}
}
