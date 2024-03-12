package com.example.project.controller;

import com.example.project.exeption.BusinessException;
import com.example.project.model.album.Album;
import com.example.project.model.comment.Comment;
import com.example.project.service.AlbumService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping("/albums")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_ALBUMS_ADMIN') || hasAuthority('ROLE_ALBUMS_VIEWER')")
    public ResponseEntity<Album[]> getAlbums() throws BusinessException {
            return new ResponseEntity<>(albumService.getAlbums(), HttpStatus.OK);
    }

    @GetMapping("/albums/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_ALBUMS_ADMIN') || hasAuthority('ROLE_ALBUMS_VIEWER')")
    public ResponseEntity<Album>  getAlbum(@PathVariable String id) throws BusinessException {
        return new ResponseEntity<>(albumService.getAlbum(id), HttpStatus.OK);
    }

    @GetMapping("/albums/{id}/comments")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_ALBUMS_ADMIN') || hasAuthority('ROLE_ALBUMS_VIEWER')")
    public ResponseEntity<Comment[]> getAlbumComments(@PathVariable String id) throws BusinessException {
        return  new ResponseEntity<>(albumService.getAlbumComments(id), HttpStatus.OK);
    }

    @PostMapping("/albums")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_ALBUMS_ADMIN')")
    public ResponseEntity<Album> postAlbum(@RequestBody Album album) throws BusinessException {
        return new ResponseEntity<>(albumService.createAlbum(album), HttpStatus.CREATED);
    }

    @PutMapping("/albums/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_ALBUMS_ADMIN')")
    public ResponseEntity<Album> updateAlbum(@RequestBody Album album, @PathVariable String id) throws BusinessException {
        return new ResponseEntity<>(albumService.updateAlbum(album, id), HttpStatus.OK);
    }

    @DeleteMapping("/albums/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_ALBUMS_ADMIN')")
    public ResponseEntity<?> deleteAlbum(@PathVariable String id) {
        albumService.deleteAlbum(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
