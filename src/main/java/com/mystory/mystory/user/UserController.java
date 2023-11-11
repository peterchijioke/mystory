package com.mystory.mystory.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.mystory.mystory.shared.GenericResponse;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("/api/v1/users")
	 public GenericResponse  createUser(@RequestBody User user) {
		userService.save(user);
		return new GenericResponse("User Created");
	}
	
	
}
