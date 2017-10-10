package tel_ran.hsa.model;

import java.util.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import scala.collection.immutable.HashMap;
import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.protocols.api.RestRequest;

public class SqlModel {
	RestTemplate restTemplate = new RestTemplate();;
	String url = "http://localhost:8080";
	HttpHeaders httpHeaders = new HttpHeaders();

	public SqlModel() {
		System.err.println("New Model");
		String tokenBase64 = Base64.getEncoder().encodeToString("manager:manager".getBytes());
		httpHeaders.add("Authorization", "Basic " + tokenBase64);

	}

	public Patient getPatientById(int idPatient) {
		System.err.println("sending get req "+idPatient);
		try {
			HttpEntity<String> requestEntity = new HttpEntity<String>(httpHeaders);
			Patient pat = restTemplate.exchange(url + RestRequest.PATIENTS + String.format("/%d", idPatient), HttpMethod.GET,
					requestEntity, Patient.class).getBody();
			System.err.println("Patient = " + pat);
			return pat;
		} catch (Throwable e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public void putHeartBeatToBase(HeartBeat heartBeat) {
		System.err.println("sending post req");
		try {
//			Map<String, String> map = new LinkedHashMap<>();
//			Gson gson = new Gson();
//			String req = gson.toJson(heartBeat);
			HttpEntity<HeartBeat> requestEntity = new HttpEntity<>(heartBeat, httpHeaders);
			ResponseEntity<String> res = restTemplate.exchange(url + RestRequest.PULSE, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<String>() {
			});
			System.err.println(res.getBody());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.err.println("req is sended");
	}

}
