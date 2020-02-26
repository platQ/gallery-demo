package com.example.imagedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    private ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image findImageByUser(User user) {
        return imageRepository.findByUser(user);
    }

    public List<Image> findAllByUser(User user) {
        return imageRepository.findAllByUser(user);
    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }
}
