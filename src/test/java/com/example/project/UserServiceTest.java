package com.example.project;

import com.example.project.exeption.BusinessException;
import com.example.project.model.comment.Comment;
import com.example.project.model.user.Address;
import com.example.project.model.user.Company;
import com.example.project.model.user.Geo;
import com.example.project.model.user.User;
import com.example.project.service.UserService;
import com.example.project.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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
public class UserServiceTest {
    private UserService userService;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(restTemplate);
    }

    @Test
    public void testCreateUser_Success() throws BusinessException {
        User person = getTestUser();
        ResponseEntity<User> mockResponse = new ResponseEntity<>(person, HttpStatus.CREATED);
        when(restTemplate.exchange(Mockito.eq(JSONPLACEHOLDER_USERS_URL), Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class), Mockito.eq(User.class))).thenReturn(mockResponse);
        User createdUser = userService.createUser(person);
        assertEquals(person, createdUser);
    }

    @Test
    public void testCreateUser_Failure() {
        User person = getTestUser();

        when(restTemplate.exchange(Mockito.eq(JSONPLACEHOLDER_USERS_URL), Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class), Mockito.eq(User.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(BusinessException.class, () -> {
            userService.createUser(person);
        });
    }

    @Test
    public void testGetUser_Success() throws BusinessException {
        User user = getTestUser();
        String userId = user.getId();
        ResponseEntity<User> mockResponse = new ResponseEntity<>(user, HttpStatus.OK);
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_USERS_URL_WITH_SLASH + userId),
                Mockito.eq(User.class))).thenReturn(mockResponse);
        User retrievedUser = userService.getUser(userId);
        assertEquals(user, retrievedUser);
    }

    @Test
    public void testGetUser_Failure() {
        String userId = "2";
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_USERS_URL_WITH_SLASH + userId),
                Mockito.eq(User.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(BusinessException.class, () -> {
            userService.getUser(userId);
        });
    }

    @Test
    public void testGetUserComments_Success() throws BusinessException {
        String userId = "1";
        Comment[] comments = {new Comment("1", "2", "name1", "email", "body1"), new Comment("1", "3", "name2", "email2", "body2")};
        User user = getTestUser();
        ResponseEntity<Comment[]> mockCommentsResponse = new ResponseEntity<>(comments, HttpStatus.OK);
        ResponseEntity<User> mockUserResponse = new ResponseEntity<>(user, HttpStatus.OK);
        when(restTemplate.getForEntity(
                Mockito.eq(JSONPLACEHOLDER_USERS_URL_WITH_SLASH + userId + COMMENTS),
                Mockito.eq(Comment[].class))).thenReturn(mockCommentsResponse);
        when(restTemplate.getForEntity(
                Mockito.eq(JSONPLACEHOLDER_USERS_URL_WITH_SLASH + userId),
                Mockito.eq(User.class))).thenReturn(mockUserResponse);
        Comment[] retrievedComments = userService.getUserComments(userId);

        assertEquals(comments, retrievedComments);

    }

    @Test
    public void testGetUserComments_Failure() {
        String userId = "2";
        when(restTemplate.getForEntity(
                Mockito.eq(JSONPLACEHOLDER_USERS_URL_WITH_SLASH + userId),
                Mockito.eq(User.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(BusinessException.class, () -> {
            userService.getUserComments(userId);
        });
    }

    @Test
    public void testGetUsers_Success() throws BusinessException {
        User[] users = {getTestUser(), getTestUser()};
        ResponseEntity<User[]> mockResponse = new ResponseEntity<>(users, HttpStatus.OK);
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_USERS_URL), Mockito.eq(User[].class)))
                .thenReturn(mockResponse);

        User[] retrievedUsers = userService.getUsers();

        assertEquals(users, retrievedUsers);
    }

    @Test
    public void testGetUsers_Failure() {
        when(restTemplate.getForEntity(Mockito.eq(JSONPLACEHOLDER_USERS_URL), Mockito.eq(User[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(BusinessException.class, () -> {
            userService.getUsers();
        });

    }

    @Test
    public void testUpdateUser_Success() throws BusinessException {
        String userId = "1";
        User user = getTestUser();
        ResponseEntity<User> mockResponse = new ResponseEntity<>(user, HttpStatus.OK);
        when(restTemplate.exchange(
                Mockito.eq(JSONPLACEHOLDER_USERS_URL_WITH_SLASH + userId),
                Mockito.eq(HttpMethod.PUT),
                Mockito.any(HttpEntity.class),
                Mockito.eq(User.class))
        ).thenReturn(mockResponse);

        User updatedUser = userService.updateUser(user, userId);

        assertEquals(user, updatedUser);
    }

    @Test
    public void testUpdateUser_Failure() {
        String userId = "2";
        User user = getTestUser();
        when(restTemplate.exchange(
                Mockito.eq(JSONPLACEHOLDER_USERS_URL_WITH_SLASH + userId),
                Mockito.eq(HttpMethod.PUT),
                Mockito.any(HttpEntity.class),
                Mockito.eq(User.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(BusinessException.class, () -> {
            userService.updateUser(user, userId);
        });
    }

    @Test
    public void testDeleteUser() {
        String userId = "1";
        userService.deleteUser(userId);

        String expectedUrl = JSONPLACEHOLDER_USERS_URL_WITH_SLASH + userId;
        verify(restTemplate, times(1)).delete(Mockito.eq(expectedUrl));
    }
    private User getTestUser() {
        return new User("1",
                "name",
                "username",
                "email@april.biz",
                new Address("street", "suite", "city", "zipcode", new Geo("lat", "lng")),
                "phone",
                "web.web",
                new Company("nameCompany", "catchPhrase", "bs"));
    }


}
