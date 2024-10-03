package com.jp.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping
	public UserEntity createUser(@RequestBody UserEntity userEntity){
		UserEntity savedUserEntity = userRepository.save(userEntity);
		return savedUserEntity;
	}
	
	@GetMapping
	public List<UserEntity> getAllUsers(){
		List<UserEntity> list = userRepository.findAll();
		return list;
	}
	
	@GetMapping("/{id}")
	@Cacheable(cacheNames = "users",key = "#id")
	public UserEntity getUser(@PathVariable Integer id){
		return userRepository.findById(id).orElse(null);
	}
	
	@PutMapping("/{id}")
	@CachePut(cacheNames = "users",key = "#id")
	public UserEntity updateUser(@PathVariable Integer id,@RequestBody UserEntity userEntity){
		UserEntity user =  userRepository.findById(id).orElse(null);
		user.setFirstName(userEntity.getFirstName());
		user.setLastName(userEntity.getLastName());
		return userRepository.save(user);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	@CacheEvict(cacheNames = "users",key="#id",beforeInvocation = true)
	public void deleteUser(@PathVariable Integer id){
		 userRepository.deleteById(id);
	}

}
