package com.ecommerce.beta.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.repository.UserInfoRepository;
@Service
public class UserInfoService {
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	public List<UserInfo> loadAllUsers(){
		return userInfoRepository.findAll();
	}
	
	public Page<UserInfo>loadAllUsers(Pageable pageable){
		return userInfoRepository.findAll(pageable);
	}
//	search
	public UserInfo getUser(UUID uuid) {
		return userInfoRepository.findById(uuid).orElse(null);
	}
	
	public void updateUser(UserInfo userInfo) {
		userInfoRepository.save(userInfo);
	}
	
	public UserInfo save(UserInfo userInfo) {
		return userInfoRepository.save(userInfo);
	}
	
	public void delete(UUID uuid) {
		userInfoRepository.deleteById(uuid);
	}
	
	public Optional<UserInfo> searchUsers(String keyword) {
        return userInfoRepository.findByUsername(keyword);
    }

    public UserInfo findByEmail(String email) {
        return userInfoRepository.findByEmail(email);
    }

    public UserInfo findByUsername(String username) {
        return userInfoRepository.findByUsername(username).orElse(null);
    }

    public UserInfo findByPhone(String phone) {
        return userInfoRepository.findByPhone(phone);
    }

    public Page<UserInfo> search(String keyword, Pageable pageable) {
        return userInfoRepository.search(keyword, pageable);
    }

}
