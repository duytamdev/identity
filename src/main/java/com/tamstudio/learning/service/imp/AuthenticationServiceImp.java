package com.tamstudio.learning.service.imp;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tamstudio.learning.dto.request.AuthenticationRequest;
import com.tamstudio.learning.dto.request.IntrospectRequest;
import com.tamstudio.learning.dto.request.LogoutRequest;
import com.tamstudio.learning.dto.request.RefreshTokenRequest;
import com.tamstudio.learning.dto.response.AuthenticationResponse;
import com.tamstudio.learning.dto.response.IntrospectResponse;
import com.tamstudio.learning.entity.InvalidatedToken;
import com.tamstudio.learning.entity.User;
import com.tamstudio.learning.exception.AppException;
import com.tamstudio.learning.exception.ErrorCode;
import com.tamstudio.learning.repository.InvalidatedTokenRepository;
import com.tamstudio.learning.repository.UserRepository;
import com.tamstudio.learning.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuthenticationServiceImp implements AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @Autowired
    public AuthenticationServiceImp(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, InvalidatedTokenRepository invalidatedTokenRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected Long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected Long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${jwt.singerKey}")
    protected String SIGNER_KEY;

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        String token = introspectRequest.getToken();
//        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
//        SignedJWT signedJWT = SignedJWT.parse(token);
//        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
//
//        var verified = signedJWT.verify(jwsVerifier);
        boolean isValid = true;
        try{
            verifyToken(token);
        }catch (AppException e){
            isValid = false;
        }

        return new IntrospectResponse(isValid);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        System.out.println(authenticationRequest.getUsername() + ":username");
        User user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        }
        String token = generateToken(user,false);
        String refreshToken = generateToken(user,true);
        return new AuthenticationResponse(authenticated, token,refreshToken);
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException{
        try{
            var signToken = verifyToken(logoutRequest.getToken());
            String jti = signToken.getJWTClaimsSet().getJWTID();
            InvalidatedToken invalidatedToken = new InvalidatedToken();
            invalidatedToken.setId(jti);
            invalidatedToken.setExpiryDate(signToken.getJWTClaimsSet().getExpirationTime());
            System.out.println("id: "+invalidatedToken.getId());
            invalidatedTokenRepository.save(invalidatedToken);
        }catch (AppException e){
            System.out.println("Token already expired");
        }

    }

    private String generateToken(User user, boolean isRefreshToken) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("tamstudio")
                .issueTime(new Date())
                //.expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .expirationTime( (isRefreshToken)? new Date(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                        : new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_"+role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
                }
            });
        }
        return joiner.toString();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime =signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);
        if(!(verified && expirationTime.after(new Date()))){
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        }
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        }


       return signedJWT;
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        var signedJWTToken = verifyToken(refreshTokenRequest.getToken());
        var id = signedJWTToken.getJWTClaimsSet().getJWTID();
        //logout
        var expirationTime = signedJWTToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(id);
        invalidatedToken.setExpiryDate(expirationTime);
        invalidatedTokenRepository.save(invalidatedToken);
        //generate new token
        var username = signedJWTToken.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        String token = generateToken(user,false);
        String newRefreshToken = generateToken(user,true);

        return new AuthenticationResponse(true, token, newRefreshToken);
    }
}
