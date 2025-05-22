package com.ecommerce.beta.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.beta.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, UUID> {

    @Query("SELECT u FROM UserInfo u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<UserInfo> findByUsername(@Param("username") String username);

    UserInfo findByEmail(String email);

    UserInfo findByPhone(String phone);

    @Query(value = "SELECT * FROM user_info WHERE username LIKE :keyword% OR phone LIKE :keyword% OR phone LIKE :keyword% OR first_name LIKE :keyword% OR last_name LIKE :keyword% OR uuid like :keyword%", nativeQuery = true)
    Page<UserInfo> search(String keyword, Pageable pageable);
}