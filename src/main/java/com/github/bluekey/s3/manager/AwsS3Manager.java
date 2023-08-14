package com.github.bluekey.s3.manager;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class AwsS3Manager implements ResourceManager {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	private final AmazonS3Client amazonS3Client;

	/**
	 * s3에 파일을 업로드 합니다.
	 *
	 * @param multipartFile 업로드할 파일
	 * @param key 		 s3에 저장될 파일의 이름 혹은 경로 포함 이름
	 */
	public void upload(MultipartFile multipartFile, String key) {
		log.info("uploading file to s3: {}", key);
		ObjectMetadata objectMetadata = createObjectMetadata(multipartFile);
		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, multipartFile.getInputStream(), objectMetadata);
			amazonS3Client.putObject(putObjectRequest);
		}
		catch (AmazonS3Exception | IOException e) {
			// TODO: amazonS3에 대한 exception 처리라서 따로 에러 메세지 처리를 안해줬으나, 다른 exception과 포맷을 통일
			log.error("s3 upload error: {}", e.getMessage());
			e.getStackTrace();
		}
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

	/**
	 * s3에 저장된 파일을 삭제합니다.
	 *
	 * @param key 삭제할 파일의 이름 혹은 경로 포함 이름
	 */
	public void delete(String key) {
		log.info("deleting file from s3: {}", key);
		try {
			amazonS3Client.deleteObject(bucket, key);
		} catch (AmazonS3Exception e) {
			log.error("s3 delete error: {}", e.getMessage());
			e.getStackTrace();
		}
	}

}
