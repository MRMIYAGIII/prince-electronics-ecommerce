package com.ecommerce.beta.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Address;
import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.repository.AddressRepository;
import com.ecommerce.beta.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public void deleteByUser() {

        //userAddressRepository.deleteByUser
    }

    @Override
    public List<Address> findByUser(UserInfo user) {
        return addressRepository.findByUserInfo(user);
    }

    @Override
    public void deleteById(UUID uuid) {
        addressRepository.deleteById(uuid);
    }

    @Override
    public Address findById(UUID addressUUID) {
        return addressRepository.findById(addressUUID).orElse(null);
    }

    @Override
    public void disableAddress(UUID uuid) {
        Address userAddress = findById(uuid);
        userAddress.setEnabled(false);
        this.save(userAddress);
    }

    @Override
    public List<Address> findByUserInfoAndEnabled(UserInfo userInfo, boolean b) {
        return addressRepository.findByUserInfoAndEnabled(userInfo, b);
    }
}
