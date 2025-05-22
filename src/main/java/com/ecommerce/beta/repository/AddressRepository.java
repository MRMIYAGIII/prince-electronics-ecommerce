package com.ecommerce.beta.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.beta.entity.Address;
import com.ecommerce.beta.entity.UserInfo;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    List<Address> findByUserInfo(UserInfo user);

    List<Address> findByUserInfoAndEnabled(UserInfo userInfo, boolean b);

//    @Query(value = "DELETE from user_address WHERE user_id = :id", nativeQuery = true)
//    void deleteByUserId(@Param("id")UUID id);

}

