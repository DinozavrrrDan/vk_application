package com.example.project.service;

import com.example.project.exeption.BusinessException;
import com.example.project.model.comment.Comment;
import com.example.project.model.user.User;

public interface UserService {
    User[] getUsers() throws BusinessException;

    User getUser(String id) throws BusinessException;

    Comment[] getUserComments(String id) throws BusinessException;

    User createUser(User user) throws BusinessException;

    User updateUser(User user, String id) throws BusinessException;

    void deleteUser(String id);
}
