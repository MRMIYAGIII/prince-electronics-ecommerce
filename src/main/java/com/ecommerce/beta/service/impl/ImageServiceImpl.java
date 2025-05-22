package com.ecommerce.beta.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Image;
import com.ecommerce.beta.repository.ImageRepository;
import com.ecommerce.beta.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
	
    @Autowired
    ImageRepository imageRepository;

    public Image save(Image image) {
        return imageRepository.save(image);
    }
    public List<Image> findImagesByProductId(UUID uuid){
        return imageRepository.findByProductId(uuid);
    }

    @Override
    public void delete(UUID uuid) {
         imageRepository.deleteById(uuid);
    }

    public Image findFileNameById(UUID uuid){
       return imageRepository.findById(uuid).orElse(null);

    }


    private void handleDelete(String fileName) throws IOException {
        // Define the directory
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/uploads";

        // Get the file path
        String filePath = uploadDir + "/" + fileName;

        // Create a file object for the file to be deleted
        File file = new File(filePath);

        // Check if the file exists
        if (file.exists()) {
            // Delete the file
            file.delete();
            System.out.println(fileName + " deleted");
        } else {
            System.out.println("File not found!");
        }
    }
	

}
