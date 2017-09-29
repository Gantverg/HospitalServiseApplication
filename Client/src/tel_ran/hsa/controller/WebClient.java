package tel_ran.hsa.controller;

import java.util.*;
import java.time.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import org.springframework.web.client.RestTemplate;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.Hospital;
import tel_ran.hsa.protocols.api.*;
import tel_ran.hsa.utils.RestConfig;

@SuppressWarnings("serial")
public class WebClient extends Hospital {
	RestTemplate restTemplate;
	String URL;
	HttpHeaders headers;
	
	public WebClient(RestConfig rest) {
		super();
		restTemplate = rest.restTemplate;
		URL = rest.URL;
		headers = rest.headers;
	}

	@Override
	public String addDoctor(Doctor doctor) {
		HttpEntity<Doctor> requestEntity = new HttpEntity<>(doctor, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.DOCTOR_ADD, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String addPatient(Patient patient) {
		HttpEntity<Patient> requestEntity = new HttpEntity<>(patient, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.PATIENT_ADD, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String removeDoctor(int doctorId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(doctorId, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.DOCTOR_REMOVE, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String removePatient(int patientId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(patientId, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.PATIENT_REMOVE, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String updateDoctor(Doctor doctor) {
		HttpEntity<Doctor> requestEntity = new HttpEntity<>(doctor, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.DOCTOR_UPDATE, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String updatePatient(Patient patient) {
		HttpEntity<Patient> requestEntity = new HttpEntity<>(patient, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.DOCTOR_UPDATE, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public Doctor getDoctor(int docotrId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(docotrId, headers);
		ResponseEntity<Doctor> response = restTemplate.exchange(URL + RestRequest.DOCTOR_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Doctor>() {
				});

		return response.getBody();
	}

	@Override
	public Patient getPatient(int patientId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(patientId, headers);
		ResponseEntity<Patient> response = restTemplate.exchange(URL + RestRequest.PATIENT_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Patient>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Visit> buildSchedule(LocalDate startDate, LocalDate finishDate) {
		Map<String, String> map = new HashMap<>();
		map.put("startdate", startDate.toString());
		map.put("finishDate", finishDate.toString());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(URL + RestRequest.SCHEDULE_BUILD, HttpMethod.POST,
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
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.VISIT_BOOK, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	

	@Override
	public Iterable<Doctor> getPatientDoctors(int patientId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(patientId, headers);
		ResponseEntity<Iterable<Doctor>> response = restTemplate.exchange(URL + RestRequest.DOCTORS_PATIENT_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Doctor>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Patient> getDoctorPatients(int docotrId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(docotrId, headers);
		ResponseEntity<Iterable<Patient>> response = restTemplate.exchange(URL + RestRequest.PATIENTS_DOCTOR_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Patient>>() {
				});

		return response.getBody();
	}

	@Override
	public String cancelVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		Map<String, String> map = new HashMap<>();
		map.put("doctorId", String.valueOf(doctorId));
		map.put("patientId", String.valueOf(patientId));
		map.put("dateTime", dateTime.toString());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.VISIT_CANCEL, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	

	
	@Override
	public Iterable<Visit> getVisitsByPatient(int patientId, LocalDate beginDate, LocalDate endDate) {
		Map<String, String> map = new HashMap<>();
		map.put("patientId", String.valueOf(patientId));
		map.put("beginDate", beginDate.toString());
		map.put("endDate", endDate.toString());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(URL + RestRequest.VISITS_GET_PATIENT, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Visit> getVisitsByDoctor(int doctorId, LocalDate beginDate, LocalDate endDate) {
		Map<String, String> map = new HashMap<>();
		map.put("doctorId", String.valueOf(doctorId));
		map.put("beginDate", beginDate.toString());
		map.put("endDate", endDate.toString());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(URL + RestRequest.VISITS_GET_DOCTOR, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Visit> getFreeVisits(int doctorId, LocalDate beginDate, LocalDate endDate) {
		Map<String, String> map = new HashMap<>();
		map.put("doctorId", String.valueOf(doctorId));
		map.put("beginDate", beginDate.toString());
		map.put("endDate", endDate.toString());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(URL + RestRequest.VISITS_GET_FREE, HttpMethod.POST,
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
		Map<String, String> map = new HashMap<>();
		map.put("patientId", String.valueOf(patientId));
		map.put("begindate", beginDate.toString());
		map.put("endDate",endDate.toString());
		map.put("surveyPeriod", String.valueOf(surveyPeriod));
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<Iterable<Integer>> response = restTemplate.exchange(URL + RestRequest.PULSE_ADD, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Integer>>() {
				});

		return response.getBody();
	}

	@Override
	public String replaceVisitsDoctor(int oldDoctorId, int newDoctorId, LocalDateTime beginDateTime,
			LocalDateTime endDateTime) {
		Map<String, String> map = new HashMap<>();
		map.put("oldDoctorId", String.valueOf(oldDoctorId));
		map.put("newDoctorId", String.valueOf(newDoctorId));
		map.put("beginDateTime",beginDateTime.toString());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.VISITS_REPLACE_DOCTOR, HttpMethod.POST,
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
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.HEALTHGROUP_ADD, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String removeHealthGroup(int groupId) {
		

		HttpEntity<Integer> requestEntity = new HttpEntity<>(groupId, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.HEALTHGROUP_REMOVE, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<HealthGroup> getHealthGroups() {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<HealthGroup>> response = restTemplate.exchange(URL + RestRequest.HEALTHGROUPS_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<HealthGroup>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Patient> getPatients() {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Patient>> response = restTemplate.exchange(URL + RestRequest.PATIENTS_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Patient>>() {});

		return response.getBody();
	}

	@Override
	public Iterator<Doctor> iterator() {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Doctor>> response = restTemplate.exchange(URL + RestRequest.DOCTORS_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Doctor>>() {});

		return response.getBody().iterator();
	}

	@Override
	public String addWorkingDays(WorkingDays workingDays) {
		Map<String,String> mapBody = new HashMap<>();
		mapBody.put("daysId", String.valueOf(workingDays.getDaysId()));
		workingDays.getWorkDays().forEach(day->mapBody.put("workDay"+day.name(), day.name()));

		HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(mapBody , headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.WORKINGDAYS_ADD, HttpMethod.POST,
				requestEntity, String.class);

		return response.getBody();
	}

	@Override
	public String removeWorkingDays(int daysId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(daysId, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.WORKINGDAYS_REMOVE, HttpMethod.POST,
				requestEntity, String.class);

		return response.getBody();
	}

	@Override
	public WorkingDays getWorkingDays(int daysId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(daysId, headers);
		ResponseEntity<WorkingDays> response = restTemplate.exchange(URL + RestRequest.WORKINGDAYS_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<WorkingDays>() {
				});

		return response.getBody();
	}

	@Override
	public String setWorkingDays(int doctorId, int daysId) {
		Map<String,String> mapBody = new HashMap<>();
		mapBody.put("doctorId", String.valueOf(doctorId));
		mapBody.put("daysId", String.valueOf(daysId));
		HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(mapBody , headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.DOCTOR_WORKINGDAYS_SET, HttpMethod.POST,
				requestEntity, String.class);

		return response.getBody();
	}

	@Override
	public HealthGroup getHealthgroup(int groupId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(groupId, headers);
		ResponseEntity<HealthGroup> response = restTemplate.exchange(URL + RestRequest.HEALTHGROUP_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<HealthGroup>() {});

		return response.getBody();
	}

	@Override
	public Iterable<WorkingDays> getAllWorkingDays() {
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<WorkingDays>> response = restTemplate.exchange(URL + RestRequest.WORKINGDAYS_GET, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<WorkingDays>>() {});

		return response.getBody();
	}
	

	@Override
	public String setHealthGroup(int patientId, int groupId) {
		Map<String,String> mapBody = new HashMap<>();
		mapBody.put("patientId", String.valueOf(patientId));
		mapBody.put("groupId", String.valueOf(groupId));
		HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(mapBody , headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.PATIENT_HEALTHGROUP_SET, HttpMethod.POST,
				requestEntity, String.class);

		return response.getBody();
	}

}
