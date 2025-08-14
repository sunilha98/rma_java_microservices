package com.resourcemgmt.usermgmt.service;

import com.resourcemgmt.usermgmt.entity.BlacklistedToken;
import com.resourcemgmt.usermgmt.repository.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BlacklistedTokenService {

    @Autowired
    private BlacklistedTokenRepository repository;

    public BlacklistedToken addToken(String token) {
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        return repository.save(blacklistedToken);
    }
}