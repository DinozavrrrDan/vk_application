package com.example.project;

import com.example.project.config.Config;
import com.example.project.controller.PostController;
import com.example.project.model.album.Album;
import com.example.project.model.comment.Comment;
import com.example.project.model.post.Post;
import com.example.project.service.AlbumService;
import com.example.project.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PostController.class)
public class PostControllerTests {
    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetPosts() throws Exception {
        Post[] posts = {getTestPost(), getTestPost()};
        when(postService.getPosts()).thenReturn(posts);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is("id")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetPost() throws Exception {
        String postId = "id";
        Post post = getTestPost();
        when(postService.getPost(postId)).thenReturn(post);

        mockMvc.perform(get("/api/posts/" + postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is("id")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetPostComments() throws Exception {
        String postId = "id";
        Comment[] comments = {new Comment("1", "2", "name1", "email", "body1"), new Comment("1", "3", "name2", "email2", "body2")};
        when(postService.getPostComments(postId)).thenReturn(comments);

        mockMvc.perform(get("/api/posts/" + postId + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is("1")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testCreatePost() throws Exception {
        Post post = getTestPost();
        when(postService.createPost(any())).thenReturn(post);
        String json = objectMapper.writeValueAsString(post);
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is("id"))); // Проверка условия на поле "id"
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testUpdateAlbum() throws Exception {
        Post updtedPost = getTestPost();
        String postId = "id";
        when(postService.updatePost(any(), any())).thenReturn(updtedPost);
        mockMvc.perform(put("/api/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updtedPost))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is("id")));
    }


    private Post getTestPost() {
        return new Post("id", "title", "title", "userId");
    }

}
