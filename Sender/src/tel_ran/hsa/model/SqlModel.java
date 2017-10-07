package tel_ran.hsa.model;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import tel_ran.hsa.entities.dto.*;

public class SqlModel {
	RestTemplate restTemplate = new RestTemplate();;
	String url = "";
	
	HttpHeaders httpHeaders;
	
	public Patient getPatientById(int idPatient) {
		httpHeaders = new HttpHeaders();
		HttpEntity<String> requestEntity = new HttpEntity<String>(httpHeaders);
		
		return restTemplate.exchange(url+String.format("/id?=%d", idPatient), HttpMethod.GET, requestEntity, Patient.class).getBody();
	}

	public void putHeartBeatToBase(HeartBeat heartBeat) {
		httpHeaders = new HttpHeaders();
		HttpEntity<HeartBeat> requestEntity = new HttpEntity<HeartBeat>(heartBeat, httpHeaders);
		
		restTemplate.exchange(url+"/pulse", HttpMethod.POST, requestEntity, HeartBeat.class);
	}
	
}
