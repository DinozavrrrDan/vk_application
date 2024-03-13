package com.example.project;

import com.example.project.exeption.BusinessException;
import com.example.project.model.album.Album;
import com.example.project.model.comment.Comment;
import com.example.project.model.post.Post;
import com.example.project.service.PostService;

import com.example.project.service.impl.PostServiceImpl;
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
public class PostServiceTest {
    private PostService postService;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        postService = new PostServiceImpl(restTemplate);
    }

    @Test
    public void testCreateAlbum_Success() throws BusinessException {
        Post post = getTestPost();
        ResponseEntity<Post> mockResponse = new ResponseEntity<>(post, HttpStatus.CREATED);
        when(restTemplate.exchange(Mockito.eq(JSONPLACEHOLDER_POSTS_URL), Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class), Mockito.eq(Post.class))).thenReturn(mockResponse);
        Post postCreated = postService.createPost(post);
        assertEquals(post, postCreated);
    }

    @Test
    public void testCreateUser_ThrowsBusinessException() {
        Post post = getTestPost();
        when(restTemplate.exchange(Mockito.eq(JSONPLACEHOLDER_POSTS_URL), Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class), Mockito.eq(Post.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(BusinessException.class, () -> {
            postService.createPost(post);
        });
    }

    @Test
    public void testGetPost_Success() throws BusinessException {
        Post post = getTestPost();
        String postId = post.getId();
        ResponseEntity<Post> mockResponse = new ResponseEntity<>(post, HttpStatus.OK);
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + postId),
                Mockito.eq(Post.class))).thenReturn(mockResponse);
        Post retrievedPost = postService.getPost(postId);
        assertEquals(post, retrievedPost);
    }

    @Test
    public void testGetPost_ThrowsBusinessException() {
        String postId = "2";
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + postId),
                Mockito.eq(Post.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(BusinessException.class, () -> {
            postService.getPost(postId);
        });
    }

    @Test
    public void testGetPostComments_Success() throws BusinessException {
        String postId = "1";
        Comment[] comments = {new Comment("1", "2", "name1", "email", "body1"), new Comment("1", "3", "name2", "email2", "body2")};
        Post post = getTestPost();
        ResponseEntity<Comment[]> mockCommentsResponse = new ResponseEntity<>(comments, HttpStatus.OK);
        ResponseEntity<Post> mockUserResponse = new ResponseEntity<>(post, HttpStatus.OK);
        when(restTemplate.getForEntity(
                Mockito.eq(JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + postId + COMMENTS),
                Mockito.eq(Comment[].class))).thenReturn(mockCommentsResponse);
        when(restTemplate.getForEntity(
                Mockito.eq(JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + postId),
                Mockito.eq(Post.class))).thenReturn(mockUserResponse);
        Comment[] retrievedComments = postService.getPostComments(postId);
        assertEquals(comments, retrievedComments);
    }

    @Test
    public void testGetPOstComments_Failure() {
        String postId = "2";
        when(restTemplate.getForEntity(
                Mockito.eq(JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + postId),
                Mockito.eq(Post.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(BusinessException.class, () -> {
            postService.getPostComments(postId);
        });
    }

    @Test
    public void testGetPosts_Success() throws BusinessException {
        Post[] posts = {getTestPost(), getTestPost()};
        ResponseEntity<Post[]> mockResponse = new ResponseEntity<>(posts, HttpStatus.OK);
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_POSTS_URL), Mockito.eq(Post[].class)))
                .thenReturn(mockResponse);

        Post[] retrievedPosts = postService.getPosts();

        assertEquals(posts, retrievedPosts);
    }

    @Test
    public void testGetPosts_Failure() {
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_POSTS_URL), Mockito.eq(Post[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(BusinessException.class, () -> {
            postService.getPosts();
        });

    }

    @Test
    public void testUpdatePost_Success() throws BusinessException {
        String postId = "1";
        Post post = getTestPost();
        ResponseEntity<Post> mockResponse = new ResponseEntity<>(post, HttpStatus.OK);
        when(restTemplate.exchange(
                Mockito.eq(JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + postId),
                Mockito.eq(HttpMethod.PUT),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Post.class))
        ).thenReturn(mockResponse);

        Post updatedPost = postService.updatePost(post, postId);
        assertEquals(post, updatedPost);
    }

    @Test
    public void testUpdatePost_Failure() {
        String postId = "2";
        Post post = getTestPost();
        when(restTemplate.exchange(
                Mockito.eq(JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + postId),
                Mockito.eq(HttpMethod.PUT),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Post.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(BusinessException.class, () -> {
            postService.updatePost(post, postId);
        });
    }

    @Test
    public void testDeletePost() {
        String postId = "1";
        postService.deletePost(postId);
        String expectedUrl = JSONPLACEHOLDER_POSTS_URL_WITH_SLASH + postId;
        verify(restTemplate, times(1)).delete(Mockito.eq(expectedUrl));
    }

    private Post getTestPost() {
        return new Post("id", "title", "title", "userId");
    }

}
