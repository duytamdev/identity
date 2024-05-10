package com.tamstudio.learning.controller;

import com.nimbusds.jose.JOSEException;
import com.tamstudio.learning.dto.request.AuthenticationRequest;
import com.tamstudio.learning.dto.request.IntrospectRequest;
import com.tamstudio.learning.dto.request.LogoutRequest;
import com.tamstudio.learning.dto.request.RefreshTokenRequest;
import com.tamstudio.learning.dto.response.ApiResponse;
import com.tamstudio.learning.dto.response.AuthenticationResponse;
import com.tamstudio.learning.dto.response.IntrospectResponse;
import com.tamstudio.learning.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws ParseException, JOSEException {

        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        response.setData(authenticationService.authenticate(authenticationRequest));
        return response;
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {

        ApiResponse<IntrospectResponse> response = new ApiResponse<>();
        response.setData(authenticationService.introspect(introspectRequest));
        return response;
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {

        ApiResponse<Void> response = new ApiResponse<>();
        authenticationService.logout(logoutRequest);
        return response;
    }

    @PostMapping("/refresh-token")
    ApiResponse<AuthenticationResponse> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {

        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        response.setData(authenticationService.refreshToken(refreshTokenRequest));
        return response;
    }

}
