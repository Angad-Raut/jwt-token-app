package com.projectx.jwt.controller;

import com.projectx.jwt.elasticsearch.entity.RefreshToken;
import com.projectx.jwt.common.exceptions.TokenExpiryedException;
import com.projectx.jwt.payload.*;
import com.projectx.jwt.common.service.JwtService;
import com.projectx.jwt.elasticsearch.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<AuthResponse>> authenticate(@RequestBody AuthRequest requestDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            RefreshToken tokenDetails = refreshTokenService.saveRefreshToken(requestDto.getUsername());
            AuthResponse response = AuthResponse.builder()
                    .accessToken(tokenDetails.getAccesstoken())
                    .refreshToken(tokenDetails.getRefreshtoken())
                    .refreshTokenId(tokenDetails.getTokenid())
                    .build();
            return new ResponseEntity<>(new ResponseDto<AuthResponse>(response, null), HttpStatus.OK);
        } catch (TokenExpiryedException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseDto<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest requestDto) {
        try {
            AuthResponse response = refreshTokenService.getRefreshToken(requestDto.getUsername());
            return new ResponseEntity<>(new ResponseDto<AuthResponse>(response,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto<Boolean>> logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Boolean result = false;
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            result = true;
        }
        return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(result,""), HttpStatus.OK);
    }
}
