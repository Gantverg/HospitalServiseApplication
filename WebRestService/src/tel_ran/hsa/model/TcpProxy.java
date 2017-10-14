package tel_ran.hsa.model;

import java.io.*;
import java.net.Socket;
import java.time.*;
import java.util.*;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import tel_ran.communication.udp.*;
import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.interfaces.IHospital;
import tel_ran.hsa.protocols.ProtocolEntity;
import tel_ran.hsa.protocols.api.*;

@SuppressWarnings("serial")
public class TcpProxy implements IHospital {

	String hostName;
	int port;

	private BufferedReader reader;
	private PrintStream writer;
	private TcpResponseCode code;
	private String responseBody;
	private ObjectMapper mapper = new ObjectMapper();
	private Socket socket;

	public TcpProxy() throws IOException {
		getCurrentConnection();
	}

	private boolean getServerInfo() {
		ObjectMapper mapper = new ObjectMapper();
		try (AbstractApplicationContext ctx = new FileSystemXmlApplicationContext("web.xml")) {
			ClientUdp clientUdp = ctx.getBean(ClientUdp.class);
			clientUdp.send(UdpRequest.GET_SERVERS_INFO);
			byte[] response = clientUdp.receive();
			if (response != null) {
				ServerInfo serverInfo = mapper.readValue(response, new TypeReference<ServerInfo>() {
				});
				hostName = serverInfo.getHostName();
				port = serverInfo.getPort();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void getCurrentConnection() throws IOException {
		if (getServerInfo()) {
			socket = new Socket(hostName, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintStream(socket.getOutputStream());
		}
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private String getRequestString(TcpRequest request, Object obj) throws JsonProcessingException {
		String header = request.toString();
		String body = mapper.writeValueAsString(obj);
		return mapper.writeValueAsString(new ProtocolEntity(header, body));
	}

	private void getResponse(String request) throws Exception {
		// getCurrentConnection();
		writer.println(request);
		String response = reader.readLine();
		ProtocolEntity pe = mapper.readValue(response, ProtocolEntity.class);
		code = TcpResponseCode.valueOf(pe.getHeader());
		responseBody = pe.getBody();
	}

	private <T> Iterable<T> getIterableResponse(TcpRequest request, Object requestBody,
			TypeReference<Iterable<T>> valueType) {
		Iterable<T> result = null;
		try {
			getResponse(getRequestString(request, requestBody));
			if (code == TcpResponseCode.OK) {
				result = mapper.readValue(responseBody, valueType);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getStringResponse(TcpRequest request, Object requestBody) {
		String result = TcpResponseCode.WRONG_REQUEST_TYPE.name();
		try {
			getResponse(getRequestString(request, requestBody));
			result = mapper.readValue(responseBody, String.class);
			/*
			 * if (code == TcpResponseCode.OK) { result = RestResponseCode.OK; } else {
			 * result = mapper.readValue(responseBody, String.class); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	private <T> T getObject(TcpRequest request, Object requestBody, Class<T> valueType) {
		try {
			String requestString = getRequestString(request, requestBody);
			getResponse(requestString);
			if (code == TcpResponseCode.OK) {
				T res = mapper.readValue(responseBody, valueType);
				return res;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String addDoctor(Doctor doctor) {
		return getStringResponse(TcpRequest.ADD_DOCTOR, doctor);
	}

	@Override
	public String addPatient(Patient patient) {
		return getStringResponse(TcpRequest.ADD_PATIENT, patient);
	}

	@Override
	public String removeDoctor(int doctorId) {
		return getStringResponse(TcpRequest.REMOVE_DOCTOR, doctorId);
	}

	@Override
	public String removePatient(int patientId) {
		return getStringResponse(TcpRequest.REMOVE_PATIENT, patientId);
	}

	@Override
	public String updateDoctor(Doctor doctor) {
		return getStringResponse(TcpRequest.UPDATE_DOCTOR, doctor);
	}

	@Override
	public String updatePatient(Patient patient) {
		return getStringResponse(TcpRequest.UPDATE_PATIENT, patient);
	}

	@Override
	public Doctor getDoctor(int doctorId) {
		return getObject(TcpRequest.GET_DOCTOR, doctorId, Doctor.class);
	}

	@Override
	public Patient getPatient(int patientId) {
		return getObject(TcpRequest.GET_PATIENT, patientId, Patient.class);
	}

	@Override
	public Iterable<Visit> buildSchedule(LocalDate startDate, LocalDate finishDate) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("startDate", startDate.toString());
		requestBody.put("finishDate", finishDate.toString());
		return getIterableResponse(TcpRequest.BUILD_SCHEDULE, requestBody, new TypeReference<Iterable<Visit>>() {
		});
	}

	@Override
	public Iterable<Visit> buildScheduleByDoctor(LocalDate startDate, LocalDate finishDate, int doctorId) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("startDate", startDate.toString());
		requestBody.put("finishDate", finishDate.toString());
		requestBody.put("doctorId", String.valueOf(doctorId));
		return getIterableResponse(TcpRequest.BUILD_SCHEDULE_BY_DOCTOR, requestBody,
				new TypeReference<Iterable<Visit>>() {
				});
	}

	@Override
	public String bookVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("doctorId", String.valueOf(doctorId));
		requestBody.put("patientId", String.valueOf(patientId));
		requestBody.put("dateTime", dateTime.toString());
		return getStringResponse(TcpRequest.BOOK_VISIT, requestBody);
	}

	@Override
	public String cancelVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("doctorId", String.valueOf(doctorId));
		requestBody.put("patientId", String.valueOf(patientId));
		requestBody.put("dateTime", dateTime.toString());
		return getStringResponse(TcpRequest.CANCEL_VISIT, requestBody);
	}

	@Override
	public String replaceVisitsDoctor(int doctorId, LocalDateTime beginDateTime, LocalDateTime endDateTime) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("doctorId", String.valueOf(doctorId));
		requestBody.put("beginDateTime", beginDateTime.toString());
		requestBody.put("endDateTime", endDateTime.toString());
		return getStringResponse(TcpRequest.REPLACE_VISITS_DOCTOR, requestBody);
	}

	@Override
	public Iterable<Doctor> getPatientDoctors(int patientId) {
		return getIterableResponse(TcpRequest.GET_PATIENT_DOCTORS, patientId, new TypeReference<Iterable<Doctor>>() {
		});
	}

	@Override
	public Iterable<Patient> getDoctorPatients(int doctorId) {
		return getIterableResponse(TcpRequest.GET_DOCTOR_PATIENTS, doctorId, new TypeReference<Iterable<Patient>>() {
		});
	}

	@Override
	public Iterable<Visit> getVisitsByPatient(int patientId, LocalDate beginDate, LocalDate endDate) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("patientId", String.valueOf(patientId));
		requestBody.put("beginDate", beginDate.toString());
		requestBody.put("endDate", endDate.toString());
		return getIterableResponse(TcpRequest.GET_VISITS_BY_PATIENT, requestBody, new TypeReference<Iterable<Visit>>() {
		});
	}

	@Override
	public Iterable<Visit> getVisitsByDoctor(int doctorId, LocalDate beginDate, LocalDate endDate) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("doctorId", String.valueOf(doctorId));
		requestBody.put("beginDate", beginDate.toString());
		requestBody.put("endDate", endDate.toString());
		return getIterableResponse(TcpRequest.GET_VISITS_BY_DOCTOR, requestBody, new TypeReference<Iterable<Visit>>() {
		});
	}

	@Override
	public Iterable<Visit> getFreeVisits(int doctorId, LocalDate beginDate, LocalDate endDate) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("doctorId", String.valueOf(doctorId));
		requestBody.put("beginDate", beginDate.toString());
		requestBody.put("endDate", endDate.toString());
		return getIterableResponse(TcpRequest.GET_FREE_VISITS, requestBody, new TypeReference<Iterable<Visit>>() {
		});
	}

	@Override
	public String addPulseInfo(HeartBeat heartBeat) {
		return getStringResponse(TcpRequest.ADD_PULSE_INFO, heartBeat);
	}

	@Override
	public Iterable<Integer> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate, int surveyPeriod) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("patientId", String.valueOf(patientId));
		requestBody.put("beginDate", beginDate.toString());
		requestBody.put("endDate", endDate.toString());
		requestBody.put("surveyPeriod", String.valueOf(surveyPeriod));
		return getIterableResponse(TcpRequest.GET_PULSE_BY_PERIOD, requestBody, new TypeReference<Iterable<Integer>>() {
		});
	}

	@Override
	public String addHealthGroup(HealthGroup healthGroup) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("groupId", String.valueOf(healthGroup.getGroupId()));
		requestBody.put("groupName", healthGroup.getGroupName());
		requestBody.put("minNormalPulse", String.valueOf(healthGroup.getMinNormalPulse()));
		requestBody.put("maxNormalPulse", String.valueOf(healthGroup.getMaxNormalPulse()));
		requestBody.put("surveyPeriod", String.valueOf(healthGroup.getSurveyPeriod()));
		return getStringResponse(TcpRequest.ADD_HEALTHGROUP, requestBody);
	}

	@Override
	public String removeHealthGroup(int groupId) {
		return getStringResponse(TcpRequest.REMOVE_HEALTHGROUP, groupId);
	}

	@Override
	public Iterable<HealthGroup> getHealthGroups() {
		return getIterableResponse(TcpRequest.GET_HEALTHGROUPS, "", new TypeReference<Iterable<HealthGroup>>() {
		});
	}

	@Override
	public Iterator<Doctor> iterator() {
		return getIterableResponse(TcpRequest.GET_DOCTORS, "", new TypeReference<Iterable<Doctor>>() {
		}).iterator();
	}

	@Override
	public Iterable<Doctor> getDoctors() {
		return getIterableResponse(TcpRequest.GET_DOCTORS, "", new TypeReference<Iterable<Doctor>>() {
		});
	}

	@Override
	public Iterable<Patient> getPatients() {
		return getIterableResponse(TcpRequest.GET_PATIENTS, "", new TypeReference<Iterable<Patient>>() {
		});
	}

	@Override
	public HealthGroup getHealthgroup(int groupId) {
		return getObject(TcpRequest.GET_HEALTHGROUP, groupId, HealthGroup.class);
	}

	@Override
	public String setHealthGroup(int patientId, int groupId) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("patientId", String.valueOf(patientId));
		requestBody.put("groupId", String.valueOf(groupId));
		return getStringResponse(TcpRequest.SET_HEALTHGROUP, requestBody);
	}

	@Override
	public Iterable<Visit> getVisits(LocalDate beginDate, LocalDate endDate) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("beginDate", String.valueOf(beginDate));
		requestBody.put("endDate", String.valueOf(endDate));
		return getIterableResponse(TcpRequest.GET_VISITS, requestBody, new TypeReference<Iterable<Visit>>() {
		});
	}

	@Override
	public Iterable<HeartBeat> getPulse(int patientId, LocalDate beginDate, LocalDate endDate) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("patientId", String.valueOf(patientId));
		requestBody.put("beginDate", beginDate.toString());
		requestBody.put("endDate", endDate.toString());
		return getIterableResponse(TcpRequest.GET_PULSE, requestBody, new TypeReference<Iterable<HeartBeat>>() {
		});
	}

	@Override
	public String setTimeSlot(int doctorId, TimeSlot... slots) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("doctorId", String.valueOf(doctorId));
		int count = 0;
		ObjectMapper mapper = new ObjectMapper();
		for (TimeSlot timeSlot : slots) {
			try {
				requestBody.put("timeSlot" + count++, mapper.writeValueAsString(timeSlot));
			} catch (JsonProcessingException e) {
			}
		}
		return getStringResponse(TcpRequest.SET_TIMESLOTS, requestBody);
	}

	@Override
	public String setTherapist(int patientId, int doctorId) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("patientId", String.valueOf(patientId));
		requestBody.put("doctorId", String.valueOf(doctorId));
		return getStringResponse(TcpRequest.SET_THERAPIST, requestBody);
	}

}
