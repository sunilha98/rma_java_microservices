package com.resourcemgmt.usermgmt.repository;

import com.resourcemgmt.usermgmt.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

}