package com.tvconss.printermanagerspring;

import com.tvconss.printermanagerspring.dto.request.auth.AuthLoginRequest;
import com.tvconss.printermanagerspring.dto.request.auth.AuthRegisterRequest;
import com.tvconss.printermanagerspring.dto.response.auth.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthServiceTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testRegisterAPI() {
        AuthRegisterRequest userRegisterRequest = new AuthRegisterRequest();
        userRegisterRequest.setEmail("tranvanconkg@gmail.com");
        userRegisterRequest.setFirstName("FirstName");
        userRegisterRequest.setLastName("LastName");
        userRegisterRequest.setPassword("password");
        userRegisterRequest.setGender("male");

        ResponseEntity<AuthResponse> response = this.restTemplate.postForEntity("/auth/register",
                userRegisterRequest,
                AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void testLoginAPI() {
        AuthLoginRequest loginPayload = new AuthLoginRequest("tranvanconkg@gmail.com", "Anhnam9ce*");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("/auth/login",
                loginPayload,
                AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }
}
