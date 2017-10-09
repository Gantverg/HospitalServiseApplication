package tel_ran.hsa.controller;

import java.util.*;
import java.time.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.Hospital;
import tel_ran.hsa.protocols.api.*;
import tel_ran.hsa.utils.RestConfig;

//TODO rebuild all methods to new map reducing

@SuppressWarnings("serial")
public class WebClient extends Hospital {
	RestTemplate restTemplate;
	String URL;
	static HttpHeaders headers;
	
	public WebClient(RestConfig rest) {
		super();
		restTemplate = rest.restTemplate;
		URL = rest.URL;
		headers = rest.headers;
	}

	@Override
	public String addDoctor(Doctor doctor) {
		HttpEntity<Doctor> requestEntity = new HttpEntity<>(doctor, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.DOCTORS, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String addPatient(Patient patient) {
		HttpEntity<Patient> requestEntity = new HttpEntity<>(patient, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.PATIENTS, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String removeDoctor(int doctorId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.DOCTORS)
		        .queryParam(RestRequest.DOCTOR_ID, doctorId);
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String removePatient(int patientId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(patientId, headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.PATIENTS)
		        .queryParam(RestRequest.PATIENT_ID, patientId);
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String updateDoctor(Doctor doctor) {
		HttpEntity<Doctor> requestEntity = new HttpEntity<>(doctor, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.DOCTORS, HttpMethod.PUT, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String updatePatient(Patient patient) {
		HttpEntity<Patient> requestEntity = new HttpEntity<>(patient, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.PATIENTS, HttpMethod.PUT, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public Doctor getDoctor(int docotrId) {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.DOCTORS)
		        .queryParam(RestRequest.DOCTOR_ID, docotrId);
		
		
		ResponseEntity<Doctor> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Doctor>() {
				});

		return response.getBody();
	}

	@Override
	public Patient getPatient(int patientId) {
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		Patient pat = restTemplate.exchange(URL + RestRequest.PATIENTS + String.format("/%d", patientId), HttpMethod.GET,
				requestEntity, Patient.class).getBody();
		return pat;
//		HttpEntity requestEntity = new HttpEntity<>(headers);
//		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.PATIENTS)
//		        .queryParam(RestRequest.PATIENT_ID, patientId);
//		ResponseEntity<Patient> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
//				requestEntity, new ParameterizedTypeReference<Patient>() {
//				});
//
//		return response.getBody();
	}

	@Override
	public Iterable<Visit> buildSchedule(LocalDate startDate, LocalDate finishDate) {
		Map<String, String> map = new HashMap<>();
		map.put("startdate", startDate.toString());
		map.put("finishDate", finishDate.toString());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(URL + RestRequest.VISITS, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	@Override
	public String bookVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		Map<String, String> map = new HashMap<>();
		map.put("doctorId", String.valueOf(doctorId));
		map.put("patientId", String.valueOf(patientId));
		map.put("dateTime", dateTime.toString());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.VISITS, HttpMethod.PUT,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	

	@Override
	public Iterable<Doctor> getPatientDoctors(int patientId) {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.DOCTORS+RestRequest.PATIENTS)
		        .queryParam(RestRequest.PATIENT_ID, patientId);
		        
		ResponseEntity<Iterable<Doctor>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Doctor>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Patient> getDoctorPatients(int docotrId) {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.PATIENTS+RestRequest.DOCTORS)
		        .queryParam(RestRequest.DOCTOR_ID, docotrId);
		ResponseEntity<Iterable<Patient>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Patient>>() {
				});

		return response.getBody();
	}

	@Override
	public String cancelVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.VISITS)
		        .queryParam(RestRequest.DOCTOR_ID, doctorId)
		        .queryParam(RestRequest.PATIENT_ID, patientId)
		        .queryParam(RestRequest.DATE_TIME, dateTime);
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	

	
	@Override
	public Iterable<Visit> getVisitsByPatient(int patientId, LocalDate beginDate, LocalDate endDate) {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.VISITS)
		        .queryParam(RestRequest.PATIENT_ID, patientId)
		        .queryParam(RestRequest.BEGIN_DATE, beginDate)
		        .queryParam(RestRequest.END_DATE, endDate);
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Visit> getVisitsByDoctor(int doctorId, LocalDate beginDate, LocalDate endDate) {
		
		HttpEntity requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.VISITS)
		        .queryParam(RestRequest.DOCTOR_ID, doctorId)
		        .queryParam(RestRequest.BEGIN_DATE, beginDate)
		        .queryParam(RestRequest.END_DATE, endDate);
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Visit> getFreeVisits(int doctorId, LocalDate beginDate, LocalDate endDate) {
		
		HttpEntity requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.VISITS+RestRequest.DOCTORS+RestRequest.FREE)
		        .queryParam(RestRequest.DOCTOR_ID, doctorId)
		        .queryParam(RestRequest.BEGIN_DATE, beginDate)
		        .queryParam(RestRequest.END_DATE, endDate);
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	/*@Override
	public String addPulseInfo(int patientId, LocalDateTime dateTime, int value) {
		Map<String, String> map = new HashMap<>();
		map.put("patientId", String.valueOf(patientId));
		map.put("dateTime", dateTime.toString());
		map.put("value",String.valueOf(value));
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.PULSE_ADD, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}*/

	@Override
	public Iterable<Integer> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate, int surveyPeriod) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.PULSE)
				.queryParam(RestRequest.PATIENT_ID, patientId)
		        .queryParam(RestRequest.BEGIN_DATE,beginDate)
		        .queryParam(RestRequest.END_DATE, endDate)
		        .queryParam(RestRequest.SURVEY_PERIOD, surveyPeriod);
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Integer>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Integer>>() {});

		return response.getBody();
	}

	@Override
	public String replaceVisitsDoctor(int oldDoctorId, LocalDateTime beginDateTime,
			LocalDateTime endDateTime) {
		Map<String, String> map = new HashMap<>();
		map.put("oldDoctorId", String.valueOf(oldDoctorId));
		map.put("beginDateTime",beginDateTime.toString());
		map.put("endDateTime",endDateTime.toString());

		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.VISITS+RestRequest.DOCTORS, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String addHealthGroup(HealthGroup healthGroup) {
		Map<String, String> map = new HashMap<>();
		map.put("groupId",String.valueOf(healthGroup.getGroupId()));
		map.put("groupName",healthGroup.getGroupName());
		map.put("minNormalPulse", String.valueOf(healthGroup.getMinNormalPulse()));
		map.put("maxNormalPulse",String.valueOf(healthGroup.getMaxNormalPulse()));
		map.put("surveyPeriod",String.valueOf(healthGroup.getSurveyPeriod()));

		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.HEALTHGROUPS, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String removeHealthGroup(int groupId) {
		

		HttpEntity requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.HEALTHGROUPS)
		        .queryParam(RestRequest.GROUP_ID, groupId);
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<HealthGroup> getHealthGroups() {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<HealthGroup>> response = restTemplate.exchange(URL + RestRequest.HEALTHGROUPS, HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<HealthGroup>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Patient> getPatients() {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Patient>> response = restTemplate.exchange(URL + RestRequest.PATIENTS, HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Patient>>() {});

		return response.getBody();
	}

	@Override
	public Iterator<Doctor> iterator() {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Doctor>> response = restTemplate.exchange(URL + RestRequest.DOCTORS, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Doctor>>() {});

		return response.getBody().iterator();
	}

	/*@Override
	public String addWorkingDays(WorkingDays workingDays) {
		Map<String,String> mapBody = new HashMap<>();
		mapBody.put("daysId", String.valueOf(workingDays.getDaysId()));
		workingDays.getWorkDays().forEach(day->mapBody.put("workDay"+day.name(), day.name()));

		HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(mapBody , headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.WORKINGDAYS_ADD, HttpMethod.POST,
				requestEntity, String.class);

		return response.getBody();
	}*/

	/*@Override
	public String removeWorkingDays(int daysId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(daysId, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.WORKINGDAYS_REMOVE, HttpMethod.POST,
				requestEntity, String.class);

		return response.getBody();
	}*/

	/*@Override
	public WorkingDays getWorkingDays(int daysId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(daysId, headers);
		ResponseEntity<WorkingDays> response = restTemplate.exchange(URL + RestRequest.WORKINGDAYS_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<WorkingDays>() {
				});

		return response.getBody();
	}*/

	/*@Override
	public String setWorkingDays(int doctorId, int daysId) {
		Map<String,String> mapBody = new HashMap<>();
		mapBody.put("doctorId", String.valueOf(doctorId));
		mapBody.put("daysId", String.valueOf(daysId));
		HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(mapBody , headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.DOCTOR_WORKINGDAYS_SET, HttpMethod.POST,
				requestEntity, String.class);

		return response.getBody();
	}*/

	@Override
	public HealthGroup getHealthgroup(int groupId) {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.HEALTHGROUPS)
		        .queryParam(RestRequest.GROUP_ID,groupId);
		ResponseEntity<HealthGroup> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<HealthGroup>() {});

		return response.getBody();
	}

	/*@Override
	public Iterable<WorkingDays> getAllWorkingDays() {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<WorkingDays>> response = restTemplate.exchange(URL + RestRequest.WORKINGDAYS_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<WorkingDays>>() {});

		return response.getBody();
	}*/
	

	@Override
	public String setHealthGroup(int patientId, int groupId) {
		Map<String,String> mapBody = new HashMap<>();
		mapBody.put("patientId", String.valueOf(patientId));
		mapBody.put("groupId", String.valueOf(groupId));
		HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(mapBody , headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.HEALTHGROUPS, HttpMethod.PUT,
				requestEntity, String.class);

		return response.getBody();
	}

	@Override
	//GET
	public Iterable<Visit> getVisits(LocalDate beginDate, LocalDate endDate) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.HEALTHGROUPS)
		        .queryParam(RestRequest.BEGIN_DATE,beginDate)
		        .queryParam(RestRequest.END_DATE, endDate);
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {});

		return response.getBody();
	}

	@Override
	public Iterable<HeartBeat> getPulse(int patientId, LocalDate beginDate, LocalDate endDate) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL+RestRequest.PULSE)
				.queryParam(RestRequest.PATIENT_ID, patientId)
		        .queryParam(RestRequest.BEGIN_DATE,beginDate)
		        .queryParam(RestRequest.END_DATE, endDate);
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<HeartBeat>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<HeartBeat>>() {});

		return response.getBody();
	}

	@Override
	public String setTimeSlot(int doctorId, TimeSlot... slots) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Doctor> getDoctors() {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Doctor>> response = restTemplate.exchange(URL + RestRequest.DOCTORS, HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Doctor>>() {});

		return response.getBody();
	}

	@Override
	public String setTherapist(int patientId, int doctorId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
