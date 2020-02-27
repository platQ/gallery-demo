package com.example.imagedemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByUser(User user);
    Image findByName(String name);
    List<Image> findAllByGallery(String gallery);
    List<Image> findAllByUser(User user);
}