package com.tamstudio.learning.service;

import com.nimbusds.jose.JOSEException;
import com.tamstudio.learning.dto.request.AuthenticationRequest;
import com.tamstudio.learning.dto.request.IntrospectRequest;
import com.tamstudio.learning.dto.request.LogoutRequest;
import com.tamstudio.learning.dto.request.RefreshTokenRequest;
import com.tamstudio.learning.dto.response.AuthenticationResponse;
import com.tamstudio.learning.dto.response.IntrospectResponse;

import java.text.ParseException;

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws ParseException, JOSEException;
    void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException;
}
