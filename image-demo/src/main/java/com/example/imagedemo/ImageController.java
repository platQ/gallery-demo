package com.example.imagedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.security.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class ImageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @RequestMapping(value={"/upload"}, method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ModelAndView upload() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("upload");
        return modelAndView;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Image img = new Image( file.getOriginalFilename(),file.getContentType(),file.getBytes(), user);
        imageService.saveImage(img);

        System.out.println("Image saved");
        modelAndView.setViewName("upload");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/photo/test", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] showImage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Image image = imageService.findImageByUser(user);

        return image.getPic();
    }

}