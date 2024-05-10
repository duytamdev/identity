package com.tamstudio.learning.config;

import com.nimbusds.jose.JOSEException;
import com.tamstudio.learning.dto.request.IntrospectRequest;
import com.tamstudio.learning.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJWTDecoder implements JwtDecoder {

    @Value("${jwt.singerKey}")
    private String SIGNER_KEY;

    private  AuthenticationService authenticationService;
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @Override
    public Jwt decode(String token) throws JwtException {
        try{
            IntrospectRequest introspectRequest = new IntrospectRequest();
            introspectRequest.setToken(token);
          var result =  authenticationService.introspect(introspectRequest);
            System.out.println("Result: " + result.isValid());
          if(!result.isValid()){
                throw new JwtException("Invalid token");
          }
        }catch (JOSEException | ParseException e){
            throw new JwtException("Invalid token", e);
        }
        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS256");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
