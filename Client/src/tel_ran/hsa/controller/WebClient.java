package tel_ran.hsa.controller;

import java.util.*;
import java.time.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.Hospital;
import tel_ran.hsa.protocols.api.*;
import tel_ran.hsa.utils.RestConfig;
import tel_ran.security.entities.Account;

@SuppressWarnings("serial")
public class WebClient extends Hospital implements RestRequest, AccountRequest {
	private RestTemplate restTemplate;
	private String URL;
	private HttpHeaders headers;

	public WebClient(RestConfig rest) {
		super();
		setupFields(rest);
	}
	public void setRestConfig(RestConfig rest) {
		setupFields(rest);
	}

	private void setupFields(RestConfig rest) {
		restTemplate = rest.restTemplate;
		URL = rest.URL;
		headers = rest.headers;
	}
	
	@Override
	public String addDoctor(Doctor doctor) {
		HttpEntity<Doctor> requestEntity = new HttpEntity<>(doctor, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + DOCTORS, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String addPatient(Patient patient) {
		HttpEntity<Patient> requestEntity = new HttpEntity<>(patient, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + PATIENTS, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String removeDoctor(int doctorId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(URL + DOCTORS + "/" + String.valueOf(doctorId),
				HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String removePatient(int patientId) {
		HttpEntity<Integer> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(URL + PATIENTS + "/" + String.valueOf(patientId),
				HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String updateDoctor(Doctor doctor) {
		HttpEntity<Doctor> requestEntity = new HttpEntity<>(doctor, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.DOCTORS, HttpMethod.PUT,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String updatePatient(Patient patient) {
		HttpEntity<Patient> requestEntity = new HttpEntity<>(patient, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.PATIENTS, HttpMethod.PUT,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public Doctor getDoctor(int docotrId) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Doctor> response = restTemplate.exchange(URL + DOCTORS + "/" + String.valueOf(docotrId),
				HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Doctor>() {
				});

		return response.getBody();
	}

	@Override
	public Patient getPatient(int patientId) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Patient> response = restTemplate.exchange(URL + PATIENTS + "/" + String.valueOf(patientId),
				HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Patient>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Visit> buildSchedule(LocalDate startDate, LocalDate finishDate) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		Map<String, String> map = new HashMap<>();
		map.put(BEGIN_DATE, startDate.toString());
		map.put(END_DATE, finishDate.toString());

		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(URL + VISITS + "?" + getParamString(map),
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	@Override
	public String bookVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		Map<String, String> map = new HashMap<>();
		map.put(DATE_TIME, dateTime.toString());
		ResponseEntity<String> response = restTemplate.exchange(
				URL + VISITS + DOCTORS + "/" + String.valueOf(doctorId) + PATIENTS + "/" + String.valueOf(patientId)
						+ "?" + getParamString(map),
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Doctor> getPatientDoctors(int patientId) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<Iterable<Doctor>> response = restTemplate.exchange(
				URL + PATIENTS + "/" + String.valueOf(patientId) + DOCTORS, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<Iterable<Doctor>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Patient> getDoctorPatients(int docotrId) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Patient>> response = restTemplate.exchange(
				URL + DOCTORS + "/" + String.valueOf(docotrId) + PATIENTS, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<Iterable<Patient>>() {
				});

		return response.getBody();
	}

	@Override
	public String cancelVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		Map<String, String> map = new HashMap<>();
		map.put(DATE_TIME, dateTime.toString());
		ResponseEntity<String> response = restTemplate.exchange(
				URL + VISITS + DOCTORS + "/" + String.valueOf(doctorId) + PATIENTS + "/" + String.valueOf(patientId)
						+ "?" + getParamString(map),
				HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Visit> getVisitsByPatient(int patientId, LocalDate beginDate, LocalDate endDate) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		Map<String, String> map = new HashMap<>();
		map.put(BEGIN_DATE, beginDate.toString());
		map.put(END_DATE, endDate.toString());

		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(
				URL + VISITS + PATIENTS + "/" + String.valueOf(patientId) + "?" + getParamString(map), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Visit> getVisitsByDoctor(int doctorId, LocalDate beginDate, LocalDate endDate) {

		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		Map<String, String> map = new HashMap<>();
		map.put(BEGIN_DATE, beginDate.toString());
		map.put(END_DATE, endDate.toString());
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(
				URL + VISITS + DOCTORS + "/" + String.valueOf(doctorId) + "?" + getParamString(map), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Visit> getFreeVisits(int doctorId, LocalDate beginDate, LocalDate endDate) {

		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		Map<String, String> map = new HashMap<>();
		map.put(BEGIN_DATE, beginDate.toString());
		map.put(END_DATE, endDate.toString());
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(
				URL + VISITS + DOCTORS + "/" + String.valueOf(doctorId) + FREE + "?" + getParamString(map),
				HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	/*
	 * @Override public String addPulseInfo(int patientId, LocalDateTime dateTime,
	 * int value) { Map<String, String> map = new HashMap<>(); map.put("patientId",
	 * String.valueOf(patientId)); map.put("dateTime", dateTime.toString());
	 * map.put("value",String.valueOf(value)); HttpEntity<Map<String, String>>
	 * requestEntity = new HttpEntity<>(map, headers); ResponseEntity<String>
	 * response = restTemplate.exchange(URL + RestRequest.PULSE_ADD,
	 * HttpMethod.POST, requestEntity, new ParameterizedTypeReference<String>() {
	 * });
	 * 
	 * return response.getBody(); }
	 */

	@Override
	public Iterable<Integer> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate, int surveyPeriod) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		Map<String, String> map = new HashMap<>();
		map.put(BEGIN_DATE, beginDate.toString());
		map.put(END_DATE, endDate.toString());
		ResponseEntity<Iterable<Integer>> response = restTemplate.exchange(
				URL + PULSE + "/" + String.valueOf(patientId) + "/" + String.valueOf(surveyPeriod) + "/?"
						+ getParamString(map),
				HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Iterable<Integer>>() {
				});

		return response.getBody();
	}

	@Override
	public String addHealthGroup(HealthGroup healthGroup) {
		Map<String, String> map = new HashMap<>();
		map.put("groupId", String.valueOf(healthGroup.getGroupId()));
		map.put("groupName", healthGroup.getGroupName());
		map.put("minNormalPulse", String.valueOf(healthGroup.getMinNormalPulse()));
		map.put("maxNormalPulse", String.valueOf(healthGroup.getMaxNormalPulse()));
		map.put("surveyPeriod", String.valueOf(healthGroup.getSurveyPeriod()));

		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + RestRequest.HEALTHGROUPS, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String removeHealthGroup(int groupId) {

		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + HEALTHGROUPS + "/" + String.valueOf(groupId),
				HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<HealthGroup> getHealthGroups() {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<HealthGroup>> response = restTemplate.exchange(URL + RestRequest.HEALTHGROUPS,
				HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Iterable<HealthGroup>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<Patient> getPatients() {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Patient>> response = restTemplate.exchange(URL + RestRequest.PATIENTS, HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Patient>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterator<Doctor> iterator() {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Doctor>> response = restTemplate.exchange(URL + RestRequest.DOCTORS, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Doctor>>() {
				});

		return response.getBody().iterator();
	}

	/*
	 * @Override public String addWorkingDays(WorkingDays workingDays) {
	 * Map<String,String> mapBody = new HashMap<>(); mapBody.put("daysId",
	 * String.valueOf(workingDays.getDaysId()));
	 * workingDays.getWorkDays().forEach(day->mapBody.put("workDay"+day.name(),
	 * day.name()));
	 * 
	 * HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(mapBody ,
	 * headers); ResponseEntity<String> response = restTemplate.exchange(URL +
	 * RestRequest.WORKINGDAYS_ADD, HttpMethod.POST, requestEntity, String.class);
	 * 
	 * return response.getBody(); }
	 */

	/*
	 * @Override public String removeWorkingDays(int daysId) { HttpEntity<Integer>
	 * requestEntity = new HttpEntity<>(daysId, headers); ResponseEntity<String>
	 * response = restTemplate.exchange(URL + RestRequest.WORKINGDAYS_REMOVE,
	 * HttpMethod.POST, requestEntity, String.class);
	 * 
	 * return response.getBody(); }
	 */

	/*
	 * @Override public WorkingDays getWorkingDays(int daysId) { HttpEntity<Integer>
	 * requestEntity = new HttpEntity<>(daysId, headers);
	 * ResponseEntity<WorkingDays> response = restTemplate.exchange(URL +
	 * RestRequest.WORKINGDAYS_GET, HttpMethod.POST, requestEntity, new
	 * ParameterizedTypeReference<WorkingDays>() { });
	 * 
	 * return response.getBody(); }
	 */

	/*
	 * @Override public String setWorkingDays(int doctorId, int daysId) {
	 * Map<String,String> mapBody = new HashMap<>(); mapBody.put("doctorId",
	 * String.valueOf(doctorId)); mapBody.put("daysId", String.valueOf(daysId));
	 * HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(mapBody ,
	 * headers); ResponseEntity<String> response = restTemplate.exchange(URL +
	 * RestRequest.DOCTOR_WORKINGDAYS_SET, HttpMethod.POST, requestEntity,
	 * String.class);
	 * 
	 * return response.getBody(); }
	 */

	@Override
	public HealthGroup getHealthgroup(int groupId) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<HealthGroup> response = restTemplate.exchange(URL + HEALTHGROUPS + "/" + String.valueOf(groupId),
				HttpMethod.GET, requestEntity, new ParameterizedTypeReference<HealthGroup>() {
				});

		return response.getBody();
	}

	/*
	 * @Override public Iterable<WorkingDays> getAllWorkingDays() {
	 * HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
	 * ResponseEntity<Iterable<WorkingDays>> response = restTemplate.exchange(URL +
	 * RestRequest.WORKINGDAYS_GET, HttpMethod.POST, requestEntity, new
	 * ParameterizedTypeReference<Iterable<WorkingDays>>() {});
	 * 
	 * return response.getBody(); }
	 */

	@Override
	public String setHealthGroup(int patientId, int groupId) {

		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(
				URL + PATIENTS + "/" + String.valueOf(patientId) + HEALTHGROUPS + "/" + String.valueOf(groupId),
				HttpMethod.PUT, requestEntity, String.class);

		return response.getBody();
	}

	@Override
	// GET
	public Iterable<Visit> getVisits(LocalDate beginDate, LocalDate endDate) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		Map<String, String> map = new HashMap<>();
		map.put(BEGIN_DATE, beginDate.toString());
		map.put(END_DATE, endDate.toString());
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(URL + VISITS + "?" + getParamString(map),
				HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});

		return response.getBody();
	}

	@Override
	public Iterable<HeartBeat> getPulse(int patientId, LocalDate beginDate, LocalDate endDate) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		Map<String, String> map = new HashMap<>();
		map.put(BEGIN_DATE, beginDate.toString());
		map.put(END_DATE, endDate.toString());
		ResponseEntity<Iterable<HeartBeat>> response = restTemplate.exchange(
				URL + PATIENTS + "/" + String.valueOf(patientId) + "?" + getParamString(map), HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<HeartBeat>>() {
				});

		return response.getBody();
	}

	@Override
	public String setTimeSlot(int doctorId, TimeSlot... slots) {

		try {

			ObjectMapper mapper = new ObjectMapper();
			String slotsString = mapper.writeValueAsString(slots);
			HttpEntity<String> requestEntity = new HttpEntity<>(slotsString, headers);
			ResponseEntity<String> response = restTemplate.exchange(
					URL + DOCTORS + "/" + String.valueOf(doctorId) + TIMESLOT, HttpMethod.PUT, requestEntity,
					new ParameterizedTypeReference<String>() {
					});

			return response.getBody();
		} catch (Exception e) {

			return e.getMessage();
		}

	}

	@Override
	public Iterable<Doctor> getDoctors() {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Doctor>> response = restTemplate.exchange(URL + RestRequest.DOCTORS, HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Doctor>>() {
				});

		return response.getBody();
	}

	@Override
	public String setTherapist(int patientId, int doctorId) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(
				URL + PATIENTS + "/" + String.valueOf(patientId) + DOCTORS + "/" + String.valueOf(doctorId),
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	@Override
	public String replaceVisitsDoctor(int doctorId, LocalDateTime beginDateTime, LocalDateTime endDateTime) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		Map<String, String> map = new HashMap<>();
		map.put(BEGIN_DATE_TIME, beginDateTime.toString());
		map.put(END_DATE_TIME, endDateTime.toString());

		ResponseEntity<String> response = restTemplate.exchange(
				URL + VISITS + DOCTORS + "/" + String.valueOf(doctorId) + REPLACE + "?" + getParamString(map),
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<String>() {
				});

		return response.getBody();
	}

	public String login() {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + LOGIN, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<String>() {
				});
		return response.getBody();
	}

	public Iterable<Account> getAccounts() {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Account>> response = restTemplate.exchange(URL + ACCOUNTS, HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<Iterable<Account>>() {
				});
		return response.getBody();
	}

	public String addAccount(String userName, String password) {
		Map<String, String> map = new HashMap<>();
		map.put(USERNAME, userName);
		map.put("password", password);
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + ACCOUNTS, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<String>() {
				});
		return response.getBody();
	}

	@Override
	public Iterable<Visit> buildScheduleByDoctor(LocalDate startDate, LocalDate finishDate, int doctorId) {
		Map<String, String> map = new HashMap<>();
		map.put(BEGIN_DATE, startDate.toString());
		map.put(END_DATE, finishDate.toString());
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Iterable<Visit>> response = restTemplate.exchange(
				URL + VISITS + DOCTORS + "/" + String.valueOf(doctorId) + "?" + getParamString(map), HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<Iterable<Visit>>() {
				});
		return response.getBody();
	}

}