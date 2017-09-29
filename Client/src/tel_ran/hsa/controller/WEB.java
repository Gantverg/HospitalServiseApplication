package tel_ran.hsa.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class WEB {

	static RestTemplate restTemplate = new RestTemplate();
	static String URL = "http://localhost:8080";
	HttpHeaders headers = new HttpHeaders();

	public <T, B> B post(T body, String command, ResponseEntity<B> response) {
		HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
		response = restTemplate.exchange(URL + command, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<B>() {
				});

		return response.getBody();
	}

	public <B> B get(String command, ResponseEntity<B> response) {
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		response = restTemplate.exchange(URL + command, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<B>() {
				});

		return response.getBody();
	}
}
