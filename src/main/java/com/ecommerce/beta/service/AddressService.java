package com.ecommerce.beta.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Address;
import com.ecommerce.beta.entity.UserInfo;

@Service
public interface AddressService {

    Address save(Address address);

    void deleteByUser();

    List<Address> findByUser(UserInfo user);

    void deleteById(UUID uuid);

    Address findById(UUID addressUUID);

    void disableAddress(UUID uuid);

    List<Address> findByUserInfoAndEnabled(UserInfo userInfo, boolean b);
}
