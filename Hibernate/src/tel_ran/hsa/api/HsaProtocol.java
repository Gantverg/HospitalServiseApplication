package tel_ran.hsa.api;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.*;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.interfaces.*;
import tel_ran.hsa.protocols.api.*;


public class HsaProtocol implements Protocol {
	IHospital hospital;
	
	public HsaProtocol(IHospital hospital) {
		this.hospital=hospital;
	}
	
	@Override
	public String getResponse(String request) {
		String []tokens=request.split(HsaProtocolConstants.DELIMETER);
		TcpRequest type;
		try {
			type = TcpRequest.valueOf(tokens[0]);
		} catch (Throwable e) {
			return TcpResponseCode.WRONG_REQUEST_TYPE.toString();
		}
		String response=null;
		switch(type){
		case ADD_DOCTOR:response=addDoctor(tokens);break;
		case ADD_PATIENT: response=addPatient(tokens);break;
		case REMOVE_DOCTOR: response=removeDoctor(tokens);break;
		case REMOVE_PATIENT: response=removePatient(tokens);break;
		case UPDATE_DOCTOR: response=updateDoctor(tokens);break;
		case UPDATE_PATIENT:response=updatePatient(tokens);break;
		case GET_DOCTOR: response=getDoctor(tokens);break;
		case GET_PATIENT: response=getPatient(tokens);break;
		case BUILD_SCHEDULE: response=buildSchedule(tokens);break;
		case BOOK_VISIT:response=bookVisit(tokens);break;
		case CANCEL_VISIT:response=cancelVisit(tokens);break;
		case REPLACE_VISITS_DOCTOR:response=replaceVisitsDoctor(tokens);break;
		case GET_PATIENT_DOCTORS: response=getPatientDoctors(tokens);break;
		case GET_DOCTOR_PATIENTS:response=getDoctorPatients(tokens); break;
		case GET_VISITS_BY_PATIENT: response=getVisitsByPatient(tokens);break;
		case GET_VISITS_BY_DOCTOR: response=getVisitsByDoctor(tokens);break;
		case GET_FREE_VISITS: response=getFreeVisits(tokens);break;
		case ADD_PULSE_INFO: response=addPulseInfo(tokens);break;
		case GET_PULSE_BY_PERIOD: response=getPulseByPeriod(tokens);break;
		}
		return response;
	}

	private String getPulseByPeriod(String[] tokens) {
		try {
			int patientId = Integer.parseInt(tokens[1]);
			LocalDate beginDate = LocalDate.parse(tokens[2]);
			LocalDate endDate = LocalDate.parse(tokens[3]);
			int surveyPeriod = Integer.parseInt(tokens[4]);
			Iterable<Integer> numbers = hospital.getPulseByPeriod(patientId, beginDate, endDate, surveyPeriod);
			return null;//getIterableResponseInteger(HospitalProtocolApi::PulseToString, numbers);
		}catch (Exception e) {
			return getResponseWithCode(TcpResponseCode.ERROR, e.getMessage());
		}
	}

	private String addPulseInfo(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			int patientId = Integer.parseInt(tokens[1]);
			LocalDateTime dateTime = LocalDateTime.parse(tokens[2]);
			int value = Integer.parseInt(tokens[3]);
			int surveyPeriod = Integer.parseInt(tokens[4]);
			response = hospital.addPulseInfo(new HeartBeat(patientId, dateTime, value, surveyPeriod));
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(code, response);
	}

	private String getFreeVisits(String[] tokens) {
		try {
			int doctorId = Integer.parseInt(tokens[1]);
			LocalDate beginDate = LocalDate.parse(tokens[2]);
			LocalDate endDate = LocalDate.parse(tokens[3]);
			Iterable<Visit> visits = hospital.getFreeVisits(doctorId, beginDate, endDate);
			return getIterableResponse(HospitalProtocolApi::VisitToString, visits);
		}catch (Exception e) {
			return getResponseWithCode(TcpResponseCode.ERROR, e.getMessage());
		}
	}

	private String getVisitsByDoctor(String[] tokens) {
		try {
			int doctorId = Integer.parseInt(tokens[1]);
			LocalDate beginDate = LocalDate.parse(tokens[2]);
			LocalDate endDate = LocalDate.parse(tokens[3]);
			Iterable<Visit> visits = hospital.getVisitsByDoctor(doctorId, beginDate, endDate);
			return getIterableResponse(HospitalProtocolApi::VisitToString, visits);
		}catch (Exception e) {
			return getResponseWithCode(TcpResponseCode.ERROR, e.getMessage());
		}
	}

	private String getVisitsByPatient(String[] tokens) {
		try {
			int patientId = Integer.parseInt(tokens[1]);
			LocalDate beginDate = LocalDate.parse(tokens[2]);
			LocalDate endDate = LocalDate.parse(tokens[3]);
			Iterable<Visit> visits = hospital.getVisitsByPatient(patientId, beginDate, endDate);
			return getIterableResponse(HospitalProtocolApi::VisitToString, visits);
		}catch (Exception e) {
			return getResponseWithCode(TcpResponseCode.ERROR, e.getMessage());
		}
	}

	private String getDoctorPatients(String[] tokens) {
		try {
			int id = Integer.parseInt(tokens[1]);
			Iterable<Patient> patients = hospital.getDoctorPatients(id);
			return getIterableResponse(HospitalProtocolApi::PatientToString, patients);
		}catch (Exception e) {
			return getResponseWithCode(TcpResponseCode.ERROR, e.getMessage());
		}
	}

	private String getPatientDoctors(String[] tokens) {
		try {
			int id = Integer.parseInt(tokens[1]);
			Iterable<Doctor> doctors = hospital.getPatientDoctors(id);
			return getIterableResponse(HospitalProtocolApi::DoctorToString, doctors);
		}catch (Exception e) {
			return getResponseWithCode(TcpResponseCode.ERROR, e.getMessage());
		}
	}

	private String replaceVisitsDoctor(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			int oldDoctorId =Integer.parseInt(tokens[1]);
			int newDoctorId = Integer.parseInt(tokens[2]);
			LocalDateTime beginDateTime = LocalDateTime.parse(tokens[3]);
			LocalDateTime endDateTime =  LocalDateTime.parse(tokens[4]);
			response = hospital.replaceVisitsDoctor(oldDoctorId, newDoctorId, beginDateTime, endDateTime);
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(code, response);
	}

	private String cancelVisit(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			int doctorId = Integer.parseInt(tokens[1]);
			int patientId = Integer.parseInt(tokens[2]);
			LocalDateTime dateTime = LocalDateTime.parse(tokens[3]);
			response = hospital.cancelVisit(doctorId, patientId, dateTime);
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(code, response);
	}

	private String bookVisit(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			int doctorId = Integer.parseInt(tokens[1]);
			int patientId = Integer.parseInt(tokens[2]);
			LocalDateTime dateTime = LocalDateTime.parse(tokens[3]);
			response = hospital.bookVisit(doctorId, patientId, dateTime);
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(code, response);
	}

	private String buildSchedule(String[] tokens) {
		try {
			LocalDate startDate = LocalDate.parse(tokens[1]);
			LocalDate finishDate = LocalDate.parse(tokens[2]);
			Iterable<Visit> visits = hospital.buildSchedule(startDate, finishDate);
			return getIterableResponse(HospitalProtocolApi::VisitToString, visits);
		}catch (Exception e) {
			return getResponseWithCode(TcpResponseCode.ERROR, e.getMessage());
		}
		
	}

	private String getPatient(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			int idPatient = Integer.parseInt(tokens[1]);
			Patient patient = hospital.getPatient(idPatient);
			if(patient==null){
				code=TcpResponseCode.ERROR;
				response=RestResponseCode.NO_PATIENT;
			} else {
				response = HospitalProtocolApi.PatientToString(patient);
			}
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(code, response);
	}

	private String getDoctor(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			int idDoctor = Integer.parseInt(tokens[1]);
			Doctor doctor = hospital.getDoctor(idDoctor);
			if(doctor==null){
				code=TcpResponseCode.ERROR;
				response=RestResponseCode.NO_DOCTOR;
			} else {
				response = HospitalProtocolApi.DoctorToString(doctor);
			}
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(code, response);
	}

	private String updatePatient(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			int idPatient = Integer.parseInt(tokens[1]);
			Patient patient = hospital.getPatient(idPatient);
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
		return getResponseWithCode(code, response);
	}

	private String updateDoctor(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			int idDoctor = Integer.parseInt(tokens[1]);
			Doctor doctor = hospital.getDoctor(idDoctor);
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
		return getResponseWithCode(code, response);
	}

	private String removePatient(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			int idPatient = Integer.parseInt(tokens[1]);
			Patient patient = hospital.getPatient(idPatient);
			if(patient==null){
				code=TcpResponseCode.ERROR;
				response=RestResponseCode.NO_PATIENT;
			} else {
				response = hospital.removePatient(idPatient);
			}
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(code, response);
	}

	private String removeDoctor(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			int idDoctor = Integer.parseInt(tokens[1]);
			Doctor doctor = hospital.getDoctor(idDoctor);
			if(doctor==null){
				code=TcpResponseCode.ERROR;
				response=RestResponseCode.NO_DOCTOR;
			} else {
				response = hospital.removeDoctor(idDoctor);
			}
		}catch (Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(code, response);
	}

	private String addPatient(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			Patient patient = HospitalProtocolApi.
					stringToPatient(Arrays.copyOfRange(tokens, 1, tokens.length));
			if(patient==null){
				code=TcpResponseCode.ERROR;
				response=RestResponseCode.NO_PATIENT;
			} else {
				response = hospital.addPatient(patient);
			}
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(code, response);
	}

	private String addDoctor(String[] tokens) {
		String response="";
		TcpResponseCode code=TcpResponseCode.OK;
		try {
			Doctor doctor = HospitalProtocolApi.
					stringToDoctor(Arrays.copyOfRange(tokens, 1, tokens.length));
			if(doctor==null){
				code=TcpResponseCode.ERROR;
				response=RestResponseCode.NO_DOCTOR;
			} else {
				response=hospital.addDoctor(doctor);
			}
		}catch(Exception e) {
			code=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(code, response);
	}

	private <T>String getIterableResponse
	(Function< T, String> method,Iterable<T> iterable) {
		String response=null;
		TcpResponseCode responseCode=null;
		try {
			response = StreamSupport.stream(iterable.spliterator(), false)
					.map(method)
					.collect(Collectors.joining(HsaProtocolConstants.LINE_DELIMETER));
			responseCode=TcpResponseCode.OK;
		} catch (Exception e) {
			responseCode=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(responseCode,response);
	}
	
	private String getIterableResponseInteger
	(Function< Integer, String> method,Iterable<Integer> iterable) {
		String response=null;
		TcpResponseCode responseCode=null;
		try {
			response = StreamSupport.stream(iterable.spliterator(), false)
					.map(method)
					.collect(Collectors.joining(HsaProtocolConstants.LINE_DELIMETER));
			responseCode=TcpResponseCode.OK;
		} catch (Exception e) {
			responseCode=TcpResponseCode.ERROR;
			response=e.getMessage();
		}
		return getResponseWithCode(responseCode,response);
	}
	
	private String getResponseWithCode(TcpResponseCode code, String response) {
		return code.toString()+HsaProtocolConstants.LINE_DELIMETER+response;
	}

}
