package tel_ran.hsa.model;

import java.util.Base64;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.protocols.api.RestRequest;

public class SqlModel {
	RestTemplate restTemplate = new RestTemplate();;
	String url = "http://localhost:8080";
	HttpHeaders httpHeaders = new HttpHeaders();

	public SqlModel() {
		String tokenBase64 = Base64.getEncoder().encodeToString("manager:manager".getBytes());
		httpHeaders.add("Authorization", "Basic " + tokenBase64);

	}

	public Patient getPatientById(int idPatient) {
		HttpEntity<String> requestEntity = new HttpEntity<String>(httpHeaders);

		return restTemplate.exchange(url + RestRequest.PATIENTS + String.format("/%d", idPatient), HttpMethod.GET,
				requestEntity, Patient.class).getBody();
	}

	public void putHeartBeatToBase(HeartBeat heartBeat) {
		HttpEntity<HeartBeat> requestEntity = new HttpEntity<HeartBeat>(heartBeat, httpHeaders);
		restTemplate.exchange(url + RestRequest.PULSE, HttpMethod.POST, requestEntity, HeartBeat.class);
	}

}
