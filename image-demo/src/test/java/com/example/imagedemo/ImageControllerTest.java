package com.example.imagedemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @MockBean
    private UserService userService;

    @Test
    public void showImageShouldReturnFromService() throws Exception {
        when(imageService.findByName("image")).thenReturn(
                new Image("name", "type", new byte[0], new User(), "gallery"));

        this.mockMvc.perform(get("/photo/{name}", "image"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("photo/name"))
                .andExpect(model().attributeExists("image"));
    }

    @Test
    @WithMockUser
    public void testDeleting() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.findUserByUserName("user")).thenReturn(user);

        when(imageService.findByName("image")).thenReturn(
                new Image(1L, "name", "type", "gallery", user, new byte[0]));

        this.mockMvc.perform(post("/photo/{name}/delete", "image"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/mygallery/"));
    }
}
