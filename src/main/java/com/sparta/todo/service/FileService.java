package com.sparta.todo.service;

import com.sparta.todo.apiResponse.ApiResponse;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    @org.springframework.beans.factory.annotation.Value("${file.upload-dir}")
    private String uploadDir;


    public ApiResponse<String> saveFile(MultipartFile file) throws IOException {
        String mimeType = file.getContentType();
        if(!ALLOWED_MIME_TYPES.contains(mimeType)){
            return new ApiResponse<>(HttpStatus.BAD_REQUEST,"이미지 파일만 업로드 가능합니다.",null);
        }
        try{
            Files.createDirectories(Paths.get(uploadDir));
            Path filePath = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
            Files.copy(file.getInputStream(),filePath);
            return ApiResponse.success("File uploaded successfully: " + file.getOriginalFilename());
        }catch(IOException e){
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,"파일업로드 실패",null);
        }

    }

    public File getFile(String filename){
        return new File(uploadDir + File.separator + filename);
    }
}
