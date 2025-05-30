package org.example.springkart.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImplementation implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {

        //file name of the current / original file;
        String originalFileName = image.getOriginalFilename();//this getOriginalFilename() will give us both extension as well as path  where as getName() will just give the name of the file
        //Generate a unique file name
        String randomId = UUID.randomUUID().toString();

        //mat.jpg -->  1234 --> 1234.jpg. (1234 is the randomId based on the randomID i will set the name like randomId.jpg for example 1234.jpg)
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String filePath= path + File.separator+fileName;

        //check if the path exists or not
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }

        //upload to server
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return fileName;

    }
}
