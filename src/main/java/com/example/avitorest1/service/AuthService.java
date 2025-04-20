package com.example.avitorest1.service;

import com.example.avitorest1.DTO.LoginDto;
import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.request.AuthorRequest;

public interface AuthService {
    String login(LoginDto loginDto);
//    String register(AuthorEntity authorEntity);

    void register(AuthorRequest authorRequest);
    void logout();
}