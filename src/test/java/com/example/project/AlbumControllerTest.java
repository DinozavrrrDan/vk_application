package com.example.project;

import com.example.project.config.SecurityConfig;
import com.example.project.controller.AlbumController;
import com.example.project.model.album.Album;
import com.example.project.model.comment.Comment;
import com.example.project.model.user.User;
import com.example.project.service.AlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AlbumController.class)
public class AlbumControllerTest {
    @MockBean
    private AlbumService albumService;

    @Autowired
    private MockMvc mockMvc;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetAlbums() throws Exception {
        Album[] albums = {getTestAlbum(), getTestAlbum()};
        when(albumService.getAlbums()).thenReturn(albums);

        mockMvc.perform(get("/api/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is("id")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetAlbum() throws Exception {
        String albumId = "id";
        Album album = getTestAlbum();
        when(albumService.getAlbum(albumId)).thenReturn(album);

        mockMvc.perform(get("/api/albums/" + albumId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is("id")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetAlbumComments() throws Exception {
        String albumId = "id";
        Comment[] comments = {new Comment("1", "2", "name1", "email", "body1"), new Comment("1", "3", "name2", "email2", "body2")};
        when(albumService.getAlbumComments(albumId)).thenReturn(comments);

        mockMvc.perform(get("/api/albums/" + albumId + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is("1")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testPostAlbum() throws Exception {
        Album album = getTestAlbum();
        when(albumService.createAlbum(ArgumentMatchers.any())).thenReturn(album);
        String json = objectMapper.writeValueAsString(album);
        mockMvc.perform(post("/api/albums")
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
        Album updatedAlbum = getTestAlbum();
        String albumId = "id";
        when(albumService.updateAlbum(any(), any())).thenReturn(updatedAlbum);
        mockMvc.perform(put("/api/albums/" + albumId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAlbum))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is("id")));
    }

    private Album getTestAlbum() {
        return new Album("userId", "id", "title");
    }

}
