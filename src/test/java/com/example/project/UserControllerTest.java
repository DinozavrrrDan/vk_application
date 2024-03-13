package com.example.project;

import com.example.project.controller.UserController;
import com.example.project.exeption.BusinessException;
import com.example.project.model.album.Album;
import com.example.project.model.comment.Comment;
import com.example.project.model.post.Post;
import com.example.project.model.user.Address;
import com.example.project.model.user.Company;
import com.example.project.model.user.Geo;
import com.example.project.model.user.User;
import com.example.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetUsers_Success() throws Exception {
        User[] users = {getTestUser(), getTestUser()};
        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is("id")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetUser_Success() throws Exception {
        String userId = "id";
        User user = getTestUser();
        when(userService.getUser(userId)).thenReturn(user);

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is("id")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetUserComments_Success() throws Exception {
        String userId = "id";
        Comment[] comments = {new Comment("1", "2", "name1", "email", "body1"), new Comment("1", "3", "name2", "email2", "body2")};
        when(userService.getUserComments(userId)).thenReturn(comments);

        mockMvc.perform(get("/api/users/" + userId + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is("1")));
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testCreateUser_Success() throws Exception {
        User user = getTestUser();
        when(userService.createUser(any())).thenReturn(user);
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is("id")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testUpdateUser_Success() throws Exception {
        User updatedUser = getTestUser();
        String userId = "id";
        when(userService.updateUser(any(), any())).thenReturn(updatedUser);
        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is("id")));
    }


    private User getTestUser() {
        return new User("id",
                "name",
                "username",
                "email@april.biz",
                new Address("street", "suite", "city", "zipcode", new Geo("lat", "lng")),
                "phone",
                "web.web",
                new Company("nameCompany", "catchPhrase", "bs"));
    }
}
