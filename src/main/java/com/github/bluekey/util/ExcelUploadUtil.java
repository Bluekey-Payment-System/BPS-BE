package com.github.bluekey.util;

import com.amazonaws.services.s3.model.S3Object;
import com.github.bluekey.s3.manager.AwsS3Manager;
import com.github.bluekey.s3.manager.S3PrefixType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ExcelUploadUtil {

	private final AwsS3Manager awsS3Manager;

	private final S3PrefixType excel = S3PrefixType.EXCEL;

	public String uploadExcel(MultipartFile file, String key) {
		return awsS3Manager.upload(file, key, excel);
	}

	public S3Object getExcel(String filename) {
		return awsS3Manager.getS3Value(excel.getValue() + filename, excel);
	}

	public String getExcelKey(String originalFilename, String uploadAt) {
		return excel.getValue() + uploadAt + "/" + originalFilename ;
	}

	public void deleteExcel(String key) {
		awsS3Manager.delete(key, excel);
	}

}
