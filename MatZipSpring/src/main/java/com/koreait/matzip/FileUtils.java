package com.koreait.matzip;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.Part;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	public static void makeFolder(String path) {
		File dir = new File(path);
		
		if(!dir.exists()) { //폴더가 존재 하냐?
			dir.mkdirs();	//없으면 만들어라
		}
	}
	public static String getExt(String fileNm) {
		return fileNm.substring(fileNm.lastIndexOf("."));
	}
	public static String getRandomUUID(MultipartFile mf) {
		String originFileNm = mf.getOriginalFilename();
		String ext = getExt(originFileNm);
		return UUID.randomUUID() + ext;
	}
	
	public static String saveFile(String path, MultipartFile mf) {
		if(mf.isEmpty()) {return null;}
		String saveFileNm = getRandomUUID(mf);
		
		try {
			mf.transferTo(new File(path + saveFileNm));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return saveFileNm;
	}
	
	
	
	
	
	
	
	
	
	


}
