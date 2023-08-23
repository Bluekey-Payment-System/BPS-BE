package com.github.bluekey.s3.manager;

import org.springframework.web.multipart.MultipartFile;

public interface ResourceManager {
	String upload(MultipartFile multipartFile, String key, S3PrefixType type);
	void delete(String key, S3PrefixType type);
}
