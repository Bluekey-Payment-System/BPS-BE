package com.github.bluekey.s3.manager;

import org.springframework.web.multipart.MultipartFile;

public interface ResourceManager {
	void upload(MultipartFile multipartFile, String key);
	void delete(String key);
}
