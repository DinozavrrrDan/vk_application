package com.example.project.service;

import com.example.project.exeption.BusinessException;
import com.example.project.model.album.Album;
import com.example.project.model.comment.Comment;

public interface AlbumService {
    Album[] getAlbums() throws BusinessException;
    Album getAlbum(String id) throws BusinessException;
    Comment[] getAlbumComments(String id) throws BusinessException;
    Album createAlbum(Album album) throws BusinessException;
    Album updateAlbum(Album album, String id) throws BusinessException;
    void deleteAlbum(String id);
}
