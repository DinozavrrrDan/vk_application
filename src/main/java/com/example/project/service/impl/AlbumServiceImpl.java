package com.example.project.service.impl;

import com.example.project.exeption.BusinessException;
import com.example.project.model.album.Album;
import com.example.project.model.comment.Comment;
import com.example.project.service.AlbumService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


import static com.example.project.consts.CacheConsts.ALBUM_CACHE;
import static com.example.project.consts.CacheConsts.ALBUMS_CACHE;
import static com.example.project.consts.CacheConsts.ALBUM_COMMENTS;
import static com.example.project.consts.ExceptionConsts.ERROR_THEN_GET_RESPONSE;
import static com.example.project.consts.WebConsts.JSONPLACEHOLDER_ALBUMS_URL;
import static com.example.project.consts.WebConsts.JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH;
import static com.example.project.consts.WebConsts.COMMENTS;


@Service
@AllArgsConstructor
public class AlbumServiceImpl implements AlbumService {


    private final RestTemplate restTemplate;

    @Override
    @Cacheable({ALBUMS_CACHE})
    public Album[] getAlbums() throws BusinessException {
        ResponseEntity<Album[]> response;
        try {
            response = restTemplate.getForEntity(JSONPLACEHOLDER_ALBUMS_URL, Album[].class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    @Cacheable(cacheNames = {ALBUM_CACHE}, key = "#id")
    public Album getAlbum(String id) throws BusinessException {
        String getRequestUrl = JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + id;
        ResponseEntity<Album> response;
        try {
            response = restTemplate.getForEntity(getRequestUrl, Album.class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    @Cacheable(cacheNames = {ALBUM_COMMENTS}, key = "#id")
    public Comment[] getAlbumComments(String id) throws BusinessException {
        String getRequestUrlComments = JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + id + COMMENTS;
        String getRequestUrlAlbums = JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + id;
        ResponseEntity<Comment[]> response;
        try {
            restTemplate.getForEntity(getRequestUrlAlbums, Album.class);
            response = restTemplate.getForEntity(getRequestUrlComments, Comment[].class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    public Album createAlbum(Album album) throws BusinessException {
        HttpEntity<Album> request = new HttpEntity<>(album);
        ResponseEntity<Album> response;
        try {
            response = restTemplate.exchange(JSONPLACEHOLDER_ALBUMS_URL, HttpMethod.POST, request, Album.class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    @CachePut(cacheNames = {ALBUM_CACHE}, key = "#id")
    public Album updateAlbum(Album album, String id) throws BusinessException {
        String putURL = JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + id;
        HttpEntity<Album> request = new HttpEntity<>(album);
        ResponseEntity<Album> response;
        try {
            response = restTemplate.exchange(putURL, HttpMethod.PUT, request, Album.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    @CacheEvict(cacheNames = {ALBUM_CACHE}, key = "#id")
    public void deleteAlbum(String id) {
        String putURL = JSONPLACEHOLDER_ALBUMS_URL_WITH_SLASH + id;
        restTemplate.delete(putURL);
    }
}
