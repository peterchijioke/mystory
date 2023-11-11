package com.mystory.mystory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.mystory.mystory.shared.GenericResponse;
import com.mystory.mystory.user.User;
import com.mystory.mystory.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

	private String API_URL_PATH = "/api/v1/users";

	@Autowired
	UserRepository userRepository;

	@Autowired
	TestRestTemplate testRestTemplate;

	@Before
	public void cleanup() {
		userRepository.deleteAll();
	}

	@Test
	public void postUser_whenUserIsValid_receiveOk() {
		User user = createValidUser();
		ResponseEntity<Object> response = testRestTemplate.postForEntity(API_URL_PATH, user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	 @Test()
		public void postUser_whenUserIsValid_saveUserToDatabase() {
	 User user = createValidUser();
	 testRestTemplate.postForEntity(API_URL_PATH, user, Object.class);
		assertThat(userRepository.count()).isEqualTo(1);
	 }
	
	 @Test()
	 public void postUser_whenUserIsValid_receiveSuccessMessage() {
		User user = createValidUser();
	 ResponseEntity<GenericResponse> response =
	 testRestTemplate.postForEntity(API_URL_PATH, user,
	 GenericResponse.class);
	 assertThat(response.getBody()).isNotNull();
	 }

	private User createValidUser() {
		User user = new User();
		user.setFullName("test-full-name");
		user.setPassword("P4ssword");
		user.setUsername("test-username");
		return user;
	}

}
