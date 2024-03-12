package com.example.project;

import com.example.project.exeption.BusinessException;
import com.example.project.model.album.Album;
import com.example.project.model.comment.Comment;
import com.example.project.model.user.User;
import com.example.project.service.AlbumService;
import com.example.project.service.impl.AlbumServiceImpl;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.example.project.consts.WebConsts.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {
    private AlbumService albumService;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        albumService = new AlbumServiceImpl(restTemplate);
    }

    @Test
    public void testCreateAlbum_Success() throws BusinessException {
        Album album = getTestAlbum();
        ResponseEntity<Album> mockResponse = new ResponseEntity<>(album, HttpStatus.CREATED);
        when(restTemplate.exchange(Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL), Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class), Mockito.eq(Album.class))).thenReturn(mockResponse);
        Album albumCreated = albumService.createAlbum(album);
        assertEquals(album, albumCreated);
    }

    @Test
    public void testCreateUser_Failure() {
           Album album = getTestAlbum();
        when(restTemplate.exchange(Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL), Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class), Mockito.eq(Album.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(BusinessException.class, () -> {
            albumService.createAlbum(album);
        });
    }

    @Test
    public void testGetAlbum_Success() throws BusinessException {
        Album album = getTestAlbum();
        String albumId = album.getId();
        ResponseEntity<Album> mockResponse = new ResponseEntity<>(album, HttpStatus.OK);
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + albumId),
                Mockito.eq(Album.class))).thenReturn(mockResponse);
        Album retrievedAlbum = albumService.getAlbum(albumId);
        assertEquals(album, retrievedAlbum);
    }

    @Test
    public void testGetAlbum_Failure() {
        String albumId = "2";
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + albumId),
                Mockito.eq(Album.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(BusinessException.class, () -> {
            albumService.getAlbum(albumId);
        });
    }

    @Test
    public void testGetAlbumComments_Success() throws BusinessException {
        String albumId = "1";
        Comment[] comments = {new Comment("1", "2", "name1", "email", "body1"), new Comment("1", "3", "name2", "email2", "body2")};
        Album album = getTestAlbum();
        ResponseEntity<Comment[]> mockCommentsResponse = new ResponseEntity<>(comments, HttpStatus.OK);
        ResponseEntity<Album> mockUserResponse = new ResponseEntity<>(album, HttpStatus.OK);
        when(restTemplate.getForEntity(
                Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + albumId + COMMENTS),
                Mockito.eq(Comment[].class))).thenReturn(mockCommentsResponse);
        when(restTemplate.getForEntity(
                Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + albumId),
                Mockito.eq(Album.class))).thenReturn(mockUserResponse);
        Comment[] retrievedComments = albumService.getAlbumComments(albumId);
        assertEquals(comments, retrievedComments);

    }

    @Test
    public void testGetAlbumComments_Failure() {
        String albumId = "2";
        when(restTemplate.getForEntity(
                Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + albumId),
                Mockito.eq(Album.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(BusinessException.class, () -> {
            albumService.getAlbumComments(albumId);
        });
    }

    @Test
    public void testGetAlbums_Success() throws BusinessException {
        Album[] albums = {getTestAlbum(), getTestAlbum()};
        ResponseEntity<Album[]> mockResponse = new ResponseEntity<>(albums, HttpStatus.OK);
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL), Mockito.eq(Album[].class)))
                .thenReturn(mockResponse);

        Album[] retrievedAlbums = albumService.getAlbums();

        assertEquals(albums, retrievedAlbums);
    }

    @Test
    public void testGetAlbums_Failure() {
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL), Mockito.eq(Album[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(BusinessException.class, () -> {
            albumService.getAlbums();
        });

    }

    @Test
    public void testUpdateAlbum_Success() throws BusinessException {
        String userId = "1";
        Album album = getTestAlbum();
        ResponseEntity<Album> mockResponse = new ResponseEntity<>(album, HttpStatus.OK);
        when(restTemplate.exchange(
                Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + userId),
                Mockito.eq(HttpMethod.PUT),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Album.class))
        ).thenReturn(mockResponse);

        Album updatedAlbum = albumService.updateAlbum(album, userId);
        assertEquals(album, updatedAlbum);
    }

    @Test
    public void testUpdateAlbum_Failure() {
        String albumId = "2";
        Album album = getTestAlbum();
        when(restTemplate.exchange(
                Mockito.eq(JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + albumId),
                Mockito.eq(HttpMethod.PUT),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Album.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(BusinessException.class, () -> {
            albumService.updateAlbum(album, albumId);
        });
    }

    @Test
    public void testDeleteAlbum() {
        String albumId = "1";
        albumService.deleteAlbum(albumId);
        String expectedUrl = JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + albumId;
        verify(restTemplate, times(1)).delete(Mockito.eq(expectedUrl));
    }

    private Album getTestAlbum() {
        return new Album("userId", "id", "title");
    }

}
