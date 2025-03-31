package com.kamalteja.brevify.user.controller;

import com.kamalteja.brevify.baseService.handler.BaseResponseHandler;
import com.kamalteja.brevify.user.dto.AuthenticateRequestDTO;
import com.kamalteja.brevify.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.kamalteja.brevify.baseService.constants.StringConstants.SUCCESS;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticateRequestDTO authenticateRequestDTO) {
        log.info("in AuthController -> register");
        Map<String, String> response = userService.register(authenticateRequestDTO.username(), authenticateRequestDTO.password());
        return BaseResponseHandler.handler(SUCCESS, response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticateRequestDTO authenticateRequestDTO) {
        log.info("in AuthController -> login");
        Map<String, String> response = userService.authenticateAndGenerateToken(authenticateRequestDTO.username(), authenticateRequestDTO.password());
        return BaseResponseHandler.handler(SUCCESS, response);
    }
}
