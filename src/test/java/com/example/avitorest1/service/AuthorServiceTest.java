package com.example.avitorest1.service;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.enums.RoleEnum;
import com.example.avitorest1.mapper.AuthorMapper;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.repository.PostRepository;
import com.example.avitorest1.request.AuthorRequest;
import com.example.avitorest1.response.AuthorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthorService authorService;

    private AuthorEntity testAuthor;
    private AuthorRequest testAuthorRequest;
    private AuthorResponse testAuthorResponse;

    @BeforeEach
    void setUp() {
        testAuthor = new AuthorEntity();
        testAuthor.setId(1L);
        testAuthor.setUsername("testuser");
        testAuthor.setEmail("test@example.com");
        testAuthor.setPassword("password");
        testAuthor.setRole(RoleEnum.ROLE_USER);
        testAuthor.setMoney(100.0);

        testAuthorRequest = new AuthorRequest();
        testAuthorRequest.setUsername("testuser");
        testAuthorRequest.setEmail("test@example.com");
        testAuthorRequest.setPassword("password");
        testAuthorRequest.setRole(RoleEnum.ROLE_USER);

        testAuthorResponse = new AuthorResponse();
        testAuthorResponse.setId(1L);
        testAuthorResponse.setUsername("testuser");
        testAuthorResponse.setEmail("test@example.com");
        testAuthorResponse.setRole(RoleEnum.ROLE_USER);
        testAuthorResponse.setMoney(100.0);
    }

    @Test
    void createAuthor_Success() {
        when(authorRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(authorMapper.toAuthor(any(AuthorRequest.class))).thenReturn(testAuthor);
        when(authorRepository.save(any(AuthorEntity.class))).thenReturn(testAuthor);

        AuthorEntity result = authorService.createAuthor(testAuthorRequest);

        assertNotNull(result);
        assertEquals(testAuthor.getUsername(), result.getUsername());
        assertEquals(testAuthor.getEmail(), result.getEmail());
        assertEquals(testAuthor.getRole(), result.getRole());
        verify(authorRepository).save(any(AuthorEntity.class));
    }

    @Test
    void addMoney_Success() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(any(AuthorEntity.class))).thenAnswer(invocation -> {
            AuthorEntity savedAuthor = invocation.getArgument(0);
            testAuthor.setMoney(savedAuthor.getMoney());
            return testAuthor;
        });

        AuthorEntity result = authorService.addMoney(1L, 50.0);

        assertNotNull(result);
        assertEquals(150.0, result.getMoney());
        verify(authorRepository).save(any(AuthorEntity.class));
    }

    @Test
    void getAllAuthors_Success() {
        List<AuthorEntity> authors = Arrays.asList(testAuthor);
        when(authorRepository.findAll()).thenReturn(authors);

        List<AuthorEntity> result = authorService.getAllAuthors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAuthor.getId(), result.get(0).getId());
        verify(authorRepository).findAll();
    }
} 