package com.example.project.service;

import com.example.project.exeption.BusinessException;
import com.example.project.model.comment.Comment;
import com.example.project.model.post.Post;

public interface PostService {
    Post[] getPosts() throws BusinessException;
    Post getPost(String id) throws BusinessException;
    Comment[] getPostComments(String id) throws BusinessException;
    Post createPost(Post post) throws BusinessException;
    Post updatePost(Post post, String id) throws BusinessException;
    void deletePost(String id);

}
