package com.mystory.mystory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mystory.mystory.shared.GenericResponse;
import com.mystory.mystory.user.User;
import com.mystory.mystory.user.UserRepository;

/**
 * UserControllerTest
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {
	private String API_V1 = "/api/v1/users";
	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	UserRepository userRepository;

	@Before
	public void cleanup() {
		userRepository.deleteAll();
	}

	@Test
	public void postUser_whenUserIsValid_receiveOk() {
		User user = userIsValid();

		ResponseEntity<Object> response = postUser(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void postUser_whenUserIsValid_saveUserToDatabase() {
		User user = userIsValid();

		postUser(user, Object.class);
		assertThat(userRepository.count()).isNotNull();
	}

	@Test
	public void postUser_whenUserIsValid_receiveSuccessMessage() {
		User user = userIsValid();
		ResponseEntity<GenericResponse> response = postUser(user, GenericResponse.class);
		assertThat(response.getBody()).isNotNull();
	}

	@Test
	public void postUser_WhenUserIsValid_hashPasswordInDatabase() {
		User user = userIsValid();
		postUser(user, Object.class);

		List<User> users = userRepository.findAll();

		User inDb = users.get(0);

		assertThat(inDb.getPassword()).isNotEqualTo(user.getPassword());
	}

	@Test
	public void postUser_whenUserHasNullUsername_receiveBadRequest() {
		User user = userIsValid();
		user.setUsername(null);
		ResponseEntity<Object> response = postUser(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasNullFullName_receiveBadRequest() {
		User user = userIsValid();
		user.setFullName(null);
		ResponseEntity<Object> response = postUser(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasNullPassword_receiveBadRequest() {
		User user = userIsValid();
		user.setPassword(null);
		ResponseEntity<Object> response = postUser(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasPasswordWithLessThanRequired_receiveBadRequest() {
		User user = userIsValid();
		user.setPassword("P4ss");
		ResponseEntity<Object> response = postUser(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	public <T> ResponseEntity<T> postUser(Object request, Class<T> response) {
		return testRestTemplate.postForEntity(this.API_V1, request, response);
	}

	private User userIsValid() {
		User user = new User();
		user.setUsername("test-username");
		user.setFullName("test-full-name");
		user.setPassword("P4ssword");
		return user;
	}

}