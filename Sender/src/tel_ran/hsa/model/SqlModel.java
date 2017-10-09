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
		System.err.println("New Model");
		String tokenBase64 = Base64.getEncoder().encodeToString("manager:manager".getBytes());
		//httpHeaders.add("Authorization", "Basic " + tokenBase64);

	}

	public Patient getPatientById(int idPatient) {
		System.err.println("sending get req");
		HttpEntity<String> requestEntity = new HttpEntity<String>(httpHeaders);
		Patient pat = restTemplate.exchange(url + RestRequest.PATIENTS + String.format("/%d", idPatient), HttpMethod.GET,
				requestEntity, Patient.class).getBody();
		System.err.println("Patient = " + pat);
		return pat;
	}

	public void putHeartBeatToBase(HeartBeat heartBeat) {
		System.err.println("sending post req");
		HttpEntity<HeartBeat> requestEntity = new HttpEntity<HeartBeat>(heartBeat, httpHeaders);
		restTemplate.exchange(url + RestRequest.PULSE, HttpMethod.POST, requestEntity, HeartBeat.class);
		System.err.println("req is sended");
	}

}
