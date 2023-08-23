package com.github.bluekey.util;

import com.amazonaws.services.s3.model.S3Object;
import com.github.bluekey.s3.manager.AwsS3Manager;
import com.github.bluekey.s3.manager.S3PrefixType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ImageUploadUtil {

	private final AwsS3Manager awsS3Manager;
	private final S3PrefixType image = S3PrefixType.IMAGE;
	private final String S3_PROFILE_IMAGE_PREFIX = "profile/";

	public String uploadImage(MultipartFile file, String key) {
		return awsS3Manager.upload(file, key, image);
	}

	public S3Object getImage(String filename) {
		return awsS3Manager.getS3Value(image.getValue() + filename, image);
	}

	public void deleteImage(String imageUrl) {
		String key = awsS3Manager.getS3Key(imageUrl, image);
		awsS3Manager.delete(image.getValue() + key, image);
	}

	public String getProfileImageKey(String originalFilename, Long memberId) {
		return S3_PROFILE_IMAGE_PREFIX + memberId + "/" + UUID.randomUUID() + getExtension(originalFilename);
	}

	private String getExtension(String filename) {
		return filename.substring(filename.lastIndexOf("."));
	}
}
