package com.example.avitorest1.service;

import com.example.avitorest1.request.LoginRequest;


public interface AuthService {
    String login(LoginRequest loginRequest);

}