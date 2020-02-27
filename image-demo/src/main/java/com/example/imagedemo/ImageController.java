package com.example.imagedemo;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

@Controller
public class ImageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @RequestMapping(value={"/upload"}, method = RequestMethod.GET)
    public ModelAndView upload() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("upload");
        return modelAndView;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("gallery") String gallery)
            throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Image img = new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes(), user, gallery);
        imageService.saveImage(img);

        System.out.println("Image saved");
        modelAndView.setViewName("upload");
        return modelAndView;
    }

    @RequestMapping(value={"/mygallery"}, method = RequestMethod.GET)
    public ModelAndView showGallery() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        List<Image> images = imageService.findAllByUser(user);
        List<String> galleryStrings = new ArrayList<String>();

        for (Image image : images) {
            String gallery = image.getGallery();

            if (!galleryStrings.contains(gallery)) {
                galleryStrings.add(gallery);
            }
        }

        modelAndView.addObject("galleries", galleryStrings);
        modelAndView.setViewName("mygallery");
        return modelAndView;
    }

    @RequestMapping(value={"/mygallery/{gallery}"}, method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public String showSingleGallery(@PathVariable String gallery, Model model) {

        List<Image> images = imageService.findAllByGallery(gallery);
        Map<String, String> nameStringMap = new HashMap<>();

        for (Image image : images) {
            nameStringMap.put(image.getName(), Base64.encodeBase64String(image.getPic()));
        }

        model.addAttribute("imageMap", nameStringMap);

        return "mygallery/gallery";
    }

    @RequestMapping(value = "/photo/{name}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public String showImage(@PathVariable String name, Model model) {
        Image image = imageService.findByName(name);

        model.addAttribute("image", Base64.encodeBase64String(image.getPic()));

        return "photo/name";
    }

    @RequestMapping(value = "/photo/{name}/delete", method = RequestMethod.POST)
    public ModelAndView deleteImage(@PathVariable String name) {
        ModelAndView modelAndView = new ModelAndView();
        Image image = imageService.findByName(name);

        imageService.delete(image);

        //reroute back to the galleries... im not happy about doing this
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        List<Image> images = imageService.findAllByUser(user);
        List<String> galleryStrings = new ArrayList<String>();

        for (Image image1 : images) {
            String gallery = image1.getGallery();

            if (!galleryStrings.contains(gallery)) {
                galleryStrings.add(gallery);
            }
        }

        modelAndView.addObject("galleries", galleryStrings);
        modelAndView.setViewName("mygallery");
        return modelAndView;
    }
}