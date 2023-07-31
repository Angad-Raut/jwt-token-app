package com.projectx.jwt.elasticsearch.service;

import com.projectx.jwt.elasticsearch.entity.RefreshToken;
import com.projectx.jwt.mysqldb.entity.Users;
import com.projectx.jwt.common.exceptions.ResourceNotFoundException;
import com.projectx.jwt.common.exceptions.TokenExpiryedException;
import com.projectx.jwt.mysqldb.service.UserService;
import com.projectx.jwt.payload.AuthResponse;
import com.projectx.jwt.elasticsearch.repository.RefreshTokenRepository;
import com.projectx.jwt.common.service.JwtService;
import com.projectx.jwt.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TokenDetailsSearchQuery tokenDetailsSearchQuery;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public RefreshToken saveRefreshToken(String username) throws ParseException, IOException {
        Users users = userService.getUserByUserName(username);
        if (users == null) {
            throw new ResourceNotFoundException(Constants.USER_DETAILS_NOT_EXIST);
        } else {
            RefreshToken fetchData = tokenDetailsSearchQuery.getRefreshTokenByUserId(users.getId());
            if (fetchData==null) {
                Instant futureDate = Instant.ofEpochSecond(Instant.now().plus(Constants.refreshTokenExpirationDays, ChronoUnit.DAYS).getEpochSecond());
                String convertedDate = formatter.format(Date.from(Date.from(futureDate).toInstant()));
                RefreshToken tokenData = RefreshToken.builder()
                        .tokenid(UUID.randomUUID().toString())
                        .expirydate(formatter.parse(convertedDate))
                        .userid(users.getId())
                        .build();
                String accessToken = jwtService.generateToken(username);
                String refreshToken = jwtService.generateRefreshToken(users, tokenData);
                tokenData.setAccesstoken(accessToken);
                tokenData.setRefreshtoken(refreshToken);
                return refreshTokenRepository.save(tokenData);
            } else {
                validateRefreshToken(fetchData);
                return fetchData;
            }
        }
    }

    public RefreshToken validateRefreshToken(RefreshToken token) throws ParseException, IOException {
        if (isRefreshTokenExpiry(token) && isRefreshTokenExist(token.getTokenid())) {
            throw new TokenExpiryedException(Constants.REFRESH_TOKEN_EXPIRED);
        } else {
            return token;
        }
    }

    private Boolean isRefreshTokenExpiry(RefreshToken token) throws ParseException {
        Instant nowDate = Instant.ofEpochSecond(Instant.now().getEpochSecond());
        String convertedDate = formatter.format(Date.from(Date.from(nowDate).toInstant()));
        Date currentDate = formatter.parse(convertedDate);
        if (token.getExpirydate().compareTo(currentDate)<0) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean isRefreshTokenExist(String token) throws IOException {
        RefreshToken refreshToken = tokenDetailsSearchQuery.findByTokenId(token);
        if (refreshToken != null) {
            return true;
        } else {
            return false;
        }
    }

    public AuthResponse getRefreshToken(String username) throws ParseException, IOException {
        Users users = userService.getUserByUserName(username);
        if (users == null) {
            throw new ResourceNotFoundException(Constants.USER_DETAILS_NOT_EXIST);
        } else {
            RefreshToken refreshTokenDetails = tokenDetailsSearchQuery.getRefreshTokenByUserId(users.getId());
            if (refreshTokenDetails != null) {
                refreshTokenRepository.delete(refreshTokenDetails);
                Instant futureDate = Instant.ofEpochSecond(Instant.now().plus(Constants.refreshTokenExpirationDays, ChronoUnit.DAYS).getEpochSecond());
                String convertedDate = formatter.format(Date.from(Date.from(futureDate).toInstant()));
                RefreshToken requestToken = RefreshToken.builder()
                        .tokenid(UUID.randomUUID().toString())
                        .expirydate(formatter.parse(convertedDate))
                        .userid(users.getId())
                        .build();
                String accessToken = jwtService.generateToken(username);
                String refreshToken = jwtService.generateRefreshToken(users,requestToken);
                requestToken.setAccesstoken(accessToken);
                requestToken.setRefreshtoken(refreshToken);
                refreshTokenDetails =refreshTokenRepository.save(requestToken);
            }
            return AuthResponse.builder()
                    .refreshTokenId(refreshTokenDetails.getTokenid()!=null?refreshTokenDetails.getTokenid():null)
                    .accessToken(refreshTokenDetails.getAccesstoken()!=null?refreshTokenDetails.getAccesstoken():null)
                    .refreshToken(refreshTokenDetails.getRefreshtoken()!=null?refreshTokenDetails.getRefreshtoken():null)
                    .build();
        }
    }
}
