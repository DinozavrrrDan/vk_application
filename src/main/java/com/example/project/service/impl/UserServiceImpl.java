package com.example.project.service.impl;

import com.example.project.exeption.BusinessException;
import com.example.project.model.comment.Comment;
import com.example.project.model.post.Post;
import com.example.project.model.user.User;
import com.example.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static com.example.project.consts.WebConsts.ERROR_THEN_GET_RESPONSE;
import static com.example.project.consts.WebConsts.JSONPLACEHOLDER_USERS_URL;
import static com.example.project.consts.WebConsts.JSONPLACEHOLDER_USERS_URL_WITH_SLASH;
import static com.example.project.consts.WebConsts.COMMENTS;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;

    @Override
    @Cacheable({"usersCache"})
    public User[] getUsers() throws BusinessException {
        ResponseEntity<User[]> response;
        try {
            response = restTemplate.getForEntity(JSONPLACEHOLDER_USERS_URL, User[].class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    @Cacheable(cacheNames={"userCache"}, key = "#id")
    public User getUser(String id) throws BusinessException {
        String getRequestUrl  = JSONPLACEHOLDER_USERS_URL_WITH_SLASH + id;
        ResponseEntity <User> response;
        try {
            response = restTemplate.getForEntity(getRequestUrl, User.class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    public static final String JSONPLACEHOLDER_USERS_URL_WITH_SLASH = "http://jsonplaceholder.typicode.com/users/";
    public static final String COMMENTS = "/comments";

    @Override
    @Cacheable(cacheNames={"userCommentsCache"}, key = "#id")
    public Comment[] getUserComments(String id) throws BusinessException {
        String getRequestUrlComments = JSONPLACEHOLDER_USERS_URL_WITH_SLASH + id + COMMENTS;
        String getRequestUrlUser = JSONPLACEHOLDER_USERS_URL_WITH_SLASH + id;
        ResponseEntity <Comment[]> response;
        try {
            restTemplate.getForEntity(getRequestUrlUser, User.class);
            response = restTemplate.getForEntity(getRequestUrlComments, Comment[].class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    public User createUser(User user) throws BusinessException {
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<User> response;
        try {
            response = restTemplate.exchange(JSONPLACEHOLDER_USERS_URL, HttpMethod.POST, request, User.class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    @CachePut(cacheNames={"userCache"}, key = "#id")
    public User updateUser(User user, String id) throws BusinessException {
        String putURL = JSONPLACEHOLDER_USERS_URL_WITH_SLASH + id;
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<User> response;
        try {
            response = restTemplate.exchange(putURL, HttpMethod.PUT, request, User.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }


    @Override
    @CacheEvict(cacheNames={"userCache"}, key = "#id")
    public void deleteUser(String id) {
        String putURL = JSONPLACEHOLDER_USERS_URL_WITH_SLASH + id;
        restTemplate.delete(putURL);
    }
}
