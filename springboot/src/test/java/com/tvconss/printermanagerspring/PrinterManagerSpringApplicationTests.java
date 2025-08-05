package com.tvconss.printermanagerspring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PrinterManagerSpringApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void throwErrorTest() {
		ResponseEntity<String> response = this.restTemplate.getForEntity("/auth/user", String.class);

		System.out.println(response.getBody());
		System.out.println(response.getStatusCode());

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	}
}
