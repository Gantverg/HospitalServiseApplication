package tel_ran.hsa.api;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.*;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.interfaces.IHospital;
import tel_ran.hsa.protocols.ProtocolEntity;
import tel_ran.hsa.protocols.api.*;

public class HsaProtocolJson implements Protocol {
	IHospital hospital;
	private ObjectMapper mapper = new ObjectMapper();

	public HsaProtocolJson(IHospital hospital) {
		this.hospital = hospital;
	}

	private static ObjectMapper objectMapper = new ObjectMapper();

	private String getMethodName(TcpRequest type) {
		String typeStr = type.toString();
		boolean fl_capital = false;
		StringBuilder builder = new StringBuilder();
		for (char character : typeStr.toCharArray()) {
			if (character != '_') {
				if (!fl_capital) {
					character = Character.toLowerCase(character);
				} else
					fl_capital = false;
				builder.append(character);
			} else {
				fl_capital = true;
			}
		}
		return builder.toString();
	}

	private String getProtocolEntityString(TcpResponseCode code, Object obj) {
		String header = code.toString();
		String body;
		try {
			body = mapper.writeValueAsString(obj);
		} catch (Exception e) {
			header = TcpResponseCode.ERROR.toString();
			body = e.getMessage();
		}
		String res;
		try {
			res = mapper.writeValueAsString(new ProtocolEntity(header, body));
		} catch (JsonProcessingException e) {
			res = "";
		}
		return res;
	}

	@Override
	public String getResponse(String request) {

		synchronized (hospital) {
			try {
				ProtocolEntity protocolEntity = mapper.readValue(request, ProtocolEntity.class);
				TcpRequest type = TcpRequest.valueOf(protocolEntity.getHeader());
				Method method = this.getClass().getDeclaredMethod(getMethodName(type), String.class);
				String response = (String) method.invoke(this, protocolEntity.getBody());
				return response;

			} catch (Throwable e) {
				return getProtocolEntityString(TcpResponseCode.WRONG_REQUEST_TYPE, e.getMessage());
			}
		}
	}


	private String setTherapist(String request) {
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			response = hospital.setTherapist(Integer.parseInt(map.get("patientId")), 
					 						 Integer.parseInt(map.get("doctorId")));
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return objectToJson(code,response);
		
	}
	
	private String setTimeslots(String request) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		TimeSlot[] slots = new TimeSlot[map.values().size()-1];
		int count = 0;
		for (int i = 0; i < slots.length; i++) {
			slots[i] = jsonToObject(map.get("timeSlot"+count), TimeSlot.class);
			count++;
		} 
		
		try {
			response = hospital.setTimeSlot(Integer.parseInt(map.get("doctorId")), slots);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		
		return objectToJson(code,response);
	}
	
	private String getPulse(String request) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			Iterable<HeartBeat> heartbeat = hospital.getPulse(Integer.parseInt(map.get("patientId")),
					LocalDate.parse(map.get("beginDate")), LocalDate.parse(map.get("endDate")));
			return objectToJson(code, heartbeat);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
	}
	
	private String getVisits(String request) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			Iterable<Visit> visits = hospital.getVisits(LocalDate.parse(map.get("beginDate")),
					LocalDate.parse(map.get("endDate")));
			return objectToJson(code, visits);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
	}
	private String setHealthgroup(String request) {
	//	IdPatientGroup patGroup;
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			response = hospital.setHealthGroup(Integer.parseInt(map.get("patientId")),
					Integer.parseInt(map.get("groupId")));
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return objectToJson(code,response);
	}
	
	private String getHealthgroup(String request) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		int groupId = jsonToObject(request, Integer.class);
		try {
			return objectToJson(code, hospital.getHealthgroup(groupId));
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
	}
	
	private String getDoctors(String request) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			//Iterator<Doctor> iter = hospital.iterator();
			Iterable<Doctor> doctors = hospital.getDoctors();
			return objectToJson(code,doctors);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}
	private String getPatients(String request){
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			Iterable<Patient> patient = hospital.getPatients();
			return objectToJson(code,patient);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}
	private String getHealthgroups(String request){
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			Iterable<HealthGroup> group = hospital.getHealthGroups();
			return objectToJson(code,group);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}
	private String removeHealthgroup(String request) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		int idGroup = jsonToObject(request, Integer.class);
		try {
			response = hospital.removeHealthGroup(idGroup);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return objectToJson(code,response);
	}
	
	private String addHealthgroup(String request) {
	String response="";
	TcpResponseCode code=TcpResponseCode.OK;
	HealthGroup healthGroup =  jsonToObject(request, HealthGroup.class);
	try {
		response = hospital.addHealthGroup(healthGroup);
	}catch (Exception e) {
		code=TcpResponseCode.ERROR;
		response=e.getMessage();
	}
	return objectToJson(code,response);
}
	
	private String getPulseByPeriod(String request) {
		//PulsePeriod period;
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			Iterable<Integer> value = hospital.getPulseByPeriod(Integer.parseInt(map.get("patientId")),
					LocalDate.parse(map.get("beginDate")), LocalDate.parse(map.get("endDate")),
					Integer.parseInt(map.get("surveyPeriod")));
			return objectToJson(code,value);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}

	private String addPulseInfo(String request) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		HeartBeat beat = jsonToObject(request, HeartBeat.class);
		try {
			return objectToJson(code, hospital.addPulseInfo(beat));
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}

	private String getFreeVisits(String request) {
		//GetVisits visit;
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			Iterable<Visit> visits = hospital.getFreeVisits(Integer.parseInt(map.get("doctorId")),
					LocalDate.parse(map.get("beginDate")), LocalDate.parse(map.get("endDate")));
			return objectToJson(code,visits);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}

	private String getVisitsByDoctor(String request) {
	//	GetVisits visit;
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			Iterable<Visit> visits = hospital.getVisitsByDoctor(Integer.parseInt(map.get("doctorId")),
					LocalDate.parse(map.get("beginDate")), LocalDate.parse(map.get("endDate")));
			return objectToJson(code,visits);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
	}

	private String getVisitsByPatient(String request) {
		//GetVisits visit;
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			Iterable<Visit> visit = hospital.getVisitsByPatient(Integer.parseInt(map.get("patientId")),
					LocalDate.parse(map.get("beginDate")), LocalDate.parse(map.get("endDate")));
			return objectToJson(code,visit);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}

	private String getDoctorPatients(String request) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			return objectToJson(code,hospital.getDoctorPatients(jsonToObject(request, Integer.class)));
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
	}

	private String getPatientDoctors(String request) {
		int patientId = jsonToObject(request, Integer.class);
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			return objectToJson(code,hospital.getPatientDoctors(patientId));
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}

	private String replaceVisitsDoctor(String request) {
		//VisitDoctor visit;
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			response = hospital.replaceVisitsDoctor(Integer.parseInt(map.get("doctorId")), 
					LocalDateTime.parse(map.get("beginDateTime")), LocalDateTime.parse(map.get("endDateTime")));
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return objectToJson(code,response);
	}

	private String cancelVisit(String request) {
		//BookVisit cancel;
		String response="";
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			response = hospital.cancelVisit(Integer.parseInt(map.get("doctorId")),
					Integer.parseInt(map.get("patientId")), LocalDateTime.parse(map.get("dateTime")));
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return objectToJson(code,response);
	}

	private String bookVisit(String request) {
		//BookVisit bookDate;
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			response = hospital.bookVisit(Integer.parseInt(map.get("doctorId")),
					Integer.parseInt(map.get("patientId")), LocalDateTime.parse(map.get("dateTime")));
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return objectToJson(code,response);
	}

	private String buildSchedule(String request) {
		//StartFinishDate date;
		String response = "";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			Iterable<Visit> visit = hospital.buildSchedule(LocalDate.parse(map.get("startDate")), LocalDate.parse(map.get("finishDate")));
			return objectToJson(code,visit);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}
	
	private String buildScheduleByDoctor(String request) {
		//StartFinishDate date;
		String response = "";
		TcpResponseCode code=TcpResponseCode.OK;
		Map<String, String> map = new HashMap<String, String>();
		map = jsonToObject(request, new TypeReference<Map<String, String>>(){});
		try {
			Iterable<Visit> visit = hospital.buildScheduleByDoctor(LocalDate.parse(map.get("startDate")), 
														   		   LocalDate.parse(map.get("finishDate")),
														   		   Integer.valueOf(map.get("doctorId")));
			return objectToJson(code,visit);
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}

	private String getPatient(String request) {
		Patient patient = hospital.getPatient(jsonToObject(request, Integer.class));
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			if(patient==null){
				code=TcpResponseCode.ERROR;
				response=RestResponseCode.NO_PATIENT;
				return objectToJson(code,response);
			} else {
				return objectToJson(code,patient);
			}
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}

	private String getDoctor(String request) {
		Doctor doctor = hospital.getDoctor(jsonToObject(request, Integer.class));
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			if(doctor==null){
				code=TcpResponseCode.ERROR;
				response=RestResponseCode.NO_DOCTOR;
				return objectToJson(code,response);
			} else {
				return objectToJson(code,doctor);
			}
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
			return objectToJson(code,response);
		}
		
	}

	private String updatePatient(String request) {
		Patient patient = jsonToObject(request, Patient.class);
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			if(patient==null){
				code=TcpResponseCode.ERROR;
				response=RestResponseCode.NO_PATIENT;
			} else {
				response = hospital.updatePatient(patient);
			}
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return  objectToJson(code,response);
	}

	private String updateDoctor(String request) {
		Doctor doctor = jsonToObject(request, Doctor.class);
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			if(doctor==null){
				code=TcpResponseCode.ERROR;
				response=RestResponseCode.NO_DOCTOR;
			} else {
				response = hospital.updateDoctor(doctor);
			}
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return  objectToJson(code,response);
	}

	private String removePatient(String request) {
		int idPatient = jsonToObject(request, Integer.class);
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			response = hospital.removePatient(idPatient);
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage();
		}
		return objectToJson(code, response);
	}

	private String removeDoctor(String request) {
		int idDoctor = jsonToObject(request, Integer.class);
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			response = hospital.removeDoctor(idDoctor);
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage();
		}
		return objectToJson(code, response);
	}

	private String addPatient(String request) {
		Patient patient = jsonToObject(request, Patient.class);
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			if (patient == null) {
				code = TcpResponseCode.ERROR;
				response = RestResponseCode.NO_PATIENT;
			} else {
				response = hospital.addPatient(patient);
			}
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage();
		}
		return objectToJson(code, response);
	}

	private String addDoctor(String request) {
		String response = "";
		Doctor doctor = jsonToObject(request, Doctor.class);
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			if (doctor == null) {
				code = TcpResponseCode.ERROR;
				response = RestResponseCode.NO_DOCTOR;
			} else {
				response = hospital.addDoctor(doctor);
			}
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage();
		}
		return objectToJson(code, response);

	}

	public static <T> T jsonToObject(String json, Class<T> clazz) {
		T result;
		try {
			result = objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			return null;
		}
		return result;
	}

	public static <T> T jsonToObject(String json, TypeReference<T> reference) {
        T result;
        try {
            result = objectMapper.readValue(json, reference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

	
	public static String objectToJson(TcpResponseCode code, Object object) {
		String result = "";
		try {
			ProtocolEntity pe = new ProtocolEntity(code.name(), objectMapper.writeValueAsString(object));
			result = objectMapper.writeValueAsString(pe);
		} catch (JsonProcessingException e) {
			result = TcpResponseCode.ERROR.name();
		}
		return result;
	}

	public static String objectToJson(Object object) {
		String result = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			ProtocolEntity pe = new ProtocolEntity(code.name(), objectMapper.writeValueAsString(object));
			result = objectMapper.writeValueAsString(pe);
		} catch (JsonProcessingException e) {
			result = TcpResponseCode.ERROR.name();
		}
		return result;
	}
}
