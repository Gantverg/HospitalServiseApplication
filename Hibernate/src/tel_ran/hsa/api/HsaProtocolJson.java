package tel_ran.hsa.api;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.interfaces.*;
import tel_ran.hsa.protocols.ProtocolEntity;
import tel_ran.hsa.protocols.api.*;
import tel_ran.hsa.utils.*;

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

	/*
	 * @Override public String getResponse(String request) { //TODO: Build using
	 * Reflection! String[] tokens = request.split(HsaProtocolConstants.DELIMETER);
	 * TcpRequest type; try { type = TcpRequest.valueOf(tokens[0]); } catch
	 * (Throwable e) { return TcpResponseCode.WRONG_REQUEST_TYPE.toString(); }
	 * String response = null; switch (type) { case ADD_DOCTOR: response =
	 * addDoctor(request); break; case ADD_PATIENT: response = addPatient(request);
	 * break; case REMOVE_DOCTOR: response = removeDoctor(request); break; case
	 * REMOVE_PATIENT: response = removePatient(request); break; case UPDATE_DOCTOR:
	 * response = updateDoctor(request); break; case UPDATE_PATIENT: response =
	 * updatePatient(request); break; case GET_DOCTOR: response =
	 * getDoctor(request); break; case GET_PATIENT: response = getPatient(request);
	 * break; case BUILD_SCHEDULE: response = buildSchedule(request); break; case
	 * BOOK_VISIT: response = bookVisit(request); break; case CANCEL_VISIT: response
	 * = cancelVisit(request); break; case REPLACE_VISITS_DOCTOR: response =
	 * replaceVisitsDoctor(request); break; case GET_PATIENT_DOCTORS: response =
	 * getPatientDoctors(request); break; case GET_DOCTOR_PATIENTS: response =
	 * getDoctorPatients(request); break; case GET_VISITS_BY_PATIENT: response =
	 * getVisitsByPatient(request); break; case GET_VISITS_BY_DOCTOR: response =
	 * getVisitsByDoctor(request); break; case GET_FREE_VISITS: response =
	 * getFreeVisits(request); break; case ADD_PULSE_INFO: response =
	 * addPulseInfo(request); break; case GET_PULSE_BY_PERIOD: response =
	 * getPulseByPeriod(request); break; default: return
	 * TcpResponseCode.WRONG_REQUEST_TYPE.toString(); } return response; }
	 */
	private String getPulseByPeriod(String request) {
		PulsePeriod period;
		period = (PulsePeriod) jsonToObject(request, PulsePeriod.class);
		try {
			return objectToJson(hospital.getPulseByPeriod(period.getPatientId(), period.getStartDate(),
					period.getFinishDate(), period.getSurveyPeriod()));
		} catch (Exception e) {
			return objectToJson(TcpResponseCode.ERROR + e.getMessage());
		}
	}

	private String addPulseInfo(String request) {
		PulseInfo info;
		info = (PulseInfo) jsonToObject(request, PulseInfo.class);
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			response = hospital.addPulseInfo(new HeartBeat(info.getPatientId(), info.getDateTime(), info.getValue(),30)); 
			//TODO GET PATIENT SURVEY PERIOD
			return objectToJson(response);
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage() + code;
			return objectToJson(response);
		}
	}

	private String getFreeVisits(String request) {
		GetVisits visit;
		visit = (GetVisits) jsonToObject(request, GetVisits.class);
		try {
			return objectToJson(hospital.getFreeVisits(visit.getId(), visit.getStartDate(), visit.getFinishDate()));
		} catch (Exception e) {
			return objectToJson(TcpResponseCode.ERROR + e.getMessage());
		}
	}

	private String getVisitsByDoctor(String request) {
		GetVisits visit;
		visit = (GetVisits) jsonToObject(request, GetVisits.class);
		try {
			return objectToJson(hospital.getVisitsByDoctor(visit.getId(), visit.getStartDate(), visit.getFinishDate()));
		} catch (Exception e) {
			return objectToJson(TcpResponseCode.ERROR + e.getMessage());
		}
	}

	private String getVisitsByPatient(String request) {
		GetVisits visit;
		visit = (GetVisits) jsonToObject(request, GetVisits.class);
		try {
			return objectToJson(
					hospital.getVisitsByPatient(visit.getId(), visit.getStartDate(), visit.getFinishDate()));
		} catch (Exception e) {
			return objectToJson(TcpResponseCode.ERROR + e.getMessage());
		}
	}

	private String getDoctorPatients(String request) {
		try {
			return objectToJson(hospital.getDoctorPatients((int) jsonToObject(request, Integer.class)));
		} catch (Exception e) {
			return objectToJson(TcpResponseCode.ERROR + e.getMessage());
		}
	}

	private String getPatientDoctors(String request) {
		int patientId = (int) jsonToObject(request, Integer.class);
		try {
			return objectToJson(hospital.getPatientDoctors(patientId));
		} catch (Exception e) {
			return objectToJson(TcpResponseCode.ERROR + e.getMessage());
		}
	}

	private String replaceVisitsDoctor(String request) {
		VisitDoctor visit;
		visit = (VisitDoctor) jsonToObject(request, VisitDoctor.class);
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			response = hospital.replaceVisitsDoctor(visit.getDoctorId(), visit.getNewdoctorId(), visit.getStart(),
					visit.getEnd());
			return objectToJson(response);
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage() + code;
			return objectToJson(response);
		}
	}

	private String cancelVisit(String request) {
		BookVisit cancel;
		String response = "";
		cancel = (BookVisit) jsonToObject(request, BookVisit.class);
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			response = hospital.cancelVisit(cancel.getDoctorId(), cancel.getPatientId(), cancel.getDateTime());
			return objectToJson(response);
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage() + code;
			return objectToJson(response);
		}
	}

	private String bookVisit(String request) {
		BookVisit bookDate;
		bookDate = (BookVisit) jsonToObject(request, BookVisit.class);
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			response = hospital.bookVisit(bookDate.getDoctorId(), bookDate.getPatientId(), bookDate.getDateTime());
			return objectToJson(response);
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage() + code;
			return objectToJson(response);
		}
	}

	private String buildSchedule(String request) {
		StartFinishDate date;
		date = (StartFinishDate) jsonToObject(request, StartFinishDate.class);
		try {
			return objectToJson(hospital.buildSchedule(date.getStartDate(), date.getFinishDate()));
		} catch (Exception e) {
			return objectToJson(TcpResponseCode.ERROR + e.getMessage());
		}

	}

	private String getPatient(String request) {
		Patient patient = hospital.getPatient((int) jsonToObject(request, Integer.class));
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			if (patient == null) {
				code = TcpResponseCode.ERROR;
				response = RestResponseCode.NO_PATIENT + code;
				return objectToJson(response);
			} else {
				return objectToJson(patient);
			}
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage() + code;
			return objectToJson(response);
		}
	}

	private String getDoctor(String request) {
		Doctor doctor = hospital.getDoctor((int) jsonToObject(request, Integer.class));
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			if (doctor == null) {
				code = TcpResponseCode.ERROR;
				response = RestResponseCode.NO_DOCTOR;
			} else {
				response = objectMapper.writeValueAsString(doctor);
			}
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage();
		}

		return objectToJson(code, response);
	}

	private String updatePatient(String request) {
		Patient patient = (Patient) jsonToObject(request, Patient.class);
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			if (patient == null) {
				code = TcpResponseCode.ERROR;
				response = RestResponseCode.NO_PATIENT + code;
				return objectToJson(response);
			} else {
				response = hospital.updatePatient(patient);
				return objectToJson(response);
			}
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage() + code;
			return objectToJson(response);
		}
	}

	private String updateDoctor(String request) {
		Doctor doctor = (Doctor) jsonToObject(request, Doctor.class);
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			if (doctor == null) {
				code = TcpResponseCode.ERROR;
				response = RestResponseCode.NO_DOCTOR + code;
				return objectToJson(response);
			} else {
				response = hospital.updateDoctor(doctor);
				return objectToJson(response);
			}
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage() + code;
			return objectToJson(response);
		}
	}

	private String removePatient(String request) {
		int idPatient = (int) jsonToObject(request, Integer.class);
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			response = hospital.removePatient(idPatient);
			return objectToJson(code, response);
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage() + code;
			return objectToJson(code, response);
		}
	}

	private String removeDoctor(String request) {
		int idDoctor = (int) jsonToObject(request, Integer.class);
		String response = "";
		TcpResponseCode code = TcpResponseCode.OK;
		try {
			response = hospital.removeDoctor(idDoctor);
			return objectToJson(code, response);
		} catch (Exception e) {
			code = TcpResponseCode.ERROR;
			response = e.getMessage();
			return objectToJson(code, response);
		}
	}

	private String addPatient(String request) {
		Patient patient = (Patient) jsonToObject(request, Patient.class);
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
		Doctor doctor = (Doctor) jsonToObject(request, Doctor.class);
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

	public static Object jsonToObject(String json, Class aClass) {
		Object result;
		try {
			result = objectMapper.readValue(json, aClass);
		} catch (IOException e) {
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
		//TODO. Rebuild all methods to objectToJson(TcpResponseCode code, Object object)
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
