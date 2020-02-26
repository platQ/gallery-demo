package com.example.imagedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @RequestMapping(value={"/upload"}, method = RequestMethod.GET)
    public ModelAndView upload() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upload");
        return modelAndView;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        Image img = new Image( file.getOriginalFilename(),file.getContentType(),file.getBytes() );
        final Image savedImage = imageRepository.save(img);

        System.out.println("Image saved");
        modelAndView.setViewName("upload");
        return modelAndView;
    }
}