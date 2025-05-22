package com.ecommerce.beta.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Image;

@Service
public interface ImageService {
	
    Image save(Image imageEntity);
    List<Image> findImagesByProductId(UUID uuid);

    void delete(UUID uuid);
    Image findFileNameById(UUID uuid);

}
