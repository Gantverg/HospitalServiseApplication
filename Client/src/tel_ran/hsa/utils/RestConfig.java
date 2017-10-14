package tel_ran.hsa.utils;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class RestConfig {
	public RestTemplate restTemplate = new RestTemplate();
	public String URL = "http://localhost:8080";
	public HttpHeaders headers = new HttpHeaders();
	private String userPass;

	public RestConfig(String adminAuth) {
		String tokenStr = adminAuth;
		String tokenBase64 = Base64.getEncoder().encodeToString(tokenStr.getBytes());
		headers.add("Authorization", "Basic " + tokenBase64);
		headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String tokenStr) {
		userPass = tokenStr;
		headers = new HttpHeaders();
		String tokenBase64 = Base64.getEncoder().encodeToString(userPass.getBytes());
		headers.add("Authorization", "Basic " + tokenBase64);
		headers.set(HttpHeaders.CONTENT_TYPE, "application/json");

	}
}
