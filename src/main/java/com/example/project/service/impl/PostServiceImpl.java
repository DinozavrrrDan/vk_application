package com.example.project.service.impl;

import com.example.project.exeption.BusinessException;
import com.example.project.model.comment.Comment;
import com.example.project.model.post.Post;
import com.example.project.service.PostService;
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

import static com.example.project.consts.WebConsts.ERROR_THEN_GET_RESPONSE;
import static com.example.project.consts.WebConsts.JSONPLACEHOLDER_POSTS_URL;
import static com.example.project.consts.WebConsts.JSONPLACEHOLDER_POSTS_URL_WITH_SLASH;
import static com.example.project.consts.WebConsts.COMMENTS;


@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final RestTemplate restTemplate;
    @Override
    @Cacheable({"postsCache"})
    public Post[] getPosts() throws BusinessException {
        ResponseEntity<Post[]> response;
        try {
            response = restTemplate.getForEntity(JSONPLACEHOLDER_POSTS_URL, Post[].class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    @Cacheable(cacheNames = {"postCache"}, key = "#id")
    public Post getPost(String id) throws BusinessException {
        String getRequestUrl = JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + id;
        ResponseEntity<Post> response;
        try {
            response = restTemplate.getForEntity(getRequestUrl, Post.class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    @Cacheable(cacheNames = {"postCommentsCache"}, key = "#id")
    public Comment[] getPostComments(String id) throws BusinessException {
        String getRequestUrlComments = JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + id + COMMENTS;
        String getRequestUrlPost = JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + id;
        ResponseEntity<Comment[]> response;
        try {
            restTemplate.getForEntity(getRequestUrlPost, Post.class);
            response = restTemplate.getForEntity(getRequestUrlComments, Comment[].class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    public Post createPost(Post post) throws BusinessException {
        HttpEntity<Post> request = new HttpEntity<>(post);
        ResponseEntity<Post> response;
        try {
            response = restTemplate.exchange(JSONPLACEHOLDER_POSTS_URL, HttpMethod.POST, request, Post.class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    @CachePut(cacheNames = {"postCache"}, key = "#id")
    public Post updatePost(Post post, String id) throws BusinessException {
        String putURL = JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + id;
        HttpEntity<Post> request = new HttpEntity<>(post);
        ResponseEntity<Post> response;
        try {
            response = restTemplate.exchange(putURL, HttpMethod.PUT, request, Post.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new BusinessException(ERROR_THEN_GET_RESPONSE);
        }
        return response.getBody();
    }

    @Override
    @CacheEvict(cacheNames = {"postCache"}, key = "#id")
    public void deletePost(String id) {
        String putURL = JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + id;
        restTemplate.delete(putURL);
    }
}
