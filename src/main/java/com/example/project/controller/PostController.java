package com.example.project.controller;

import com.example.project.exeption.BusinessException;
import com.example.project.model.comment.Comment;
import com.example.project.model.post.Post;
import com.example.project.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_POSTS_ADMIN') || hasAuthority('ROLE_POSTS_VIEWER')")
    public ResponseEntity<Post[]> getPosts() throws BusinessException {
        return new ResponseEntity<>(postService.getPosts(), HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_POSTS_ADMIN') || hasAuthority('ROLE_POSTS_VIEWER')")
    public ResponseEntity<Post> getPost(@PathVariable String id) throws BusinessException {
        return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
    }

    @GetMapping("/posts/{id}/comments")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_POSTS_ADMIN') || hasAuthority('ROLE_POSTS_VIEWER')")
    public ResponseEntity<Comment[]> getPostComments(@PathVariable String id) throws BusinessException {
        return new ResponseEntity<>( postService.getPostComments(id), HttpStatus.OK);
    }

    @PostMapping("/posts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_POSTS_ADMIN')")
    public ResponseEntity<Post> postPost(@RequestBody Post post) throws BusinessException {
        return new ResponseEntity<>(postService.createPost(post), HttpStatus.CREATED);

    }

    @PutMapping("/posts/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_POSTS_ADMIN')")
    public ResponseEntity<Post> updatePost(@RequestBody Post post, @PathVariable String id) throws BusinessException {
        return new ResponseEntity<>(postService.updatePost(post, id), HttpStatus.OK);

    }

    @DeleteMapping("/posts/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_POSTS_ADMIN')")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
