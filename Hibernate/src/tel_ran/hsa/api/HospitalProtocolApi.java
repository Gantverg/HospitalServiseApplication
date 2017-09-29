package tel_ran.hsa.api;

import java.time.LocalDateTime;

import tel_ran.hsa.entities.dto.*;


public class HospitalProtocolApi {
	public static String PulseToString(HeartBeat pulse){
		//int id = pulse.;
		return "";

	}
	public static String PatientToString(Patient patient){
		int id = patient.getId();
		String name = patient.getName();
		String phoneNumber = patient.getPhoneNumber();
		String eMail = patient.geteMail();
		String nameGroup = patient.getHealthGroup().getGroupName();
		int minNormalPulse = patient.getHealthGroup().getMinNormalPulse();
		int maxNormalPulse = patient.getHealthGroup().getMaxNormalPulse();
		int serveyPeriod = patient.getHealthGroup().getSurveyPeriod();
		
		return id+HsaProtocolConstants.DELIMETER+name+
				HsaProtocolConstants.DELIMETER+phoneNumber+HsaProtocolConstants.DELIMETER+
				eMail+HsaProtocolConstants.DELIMETER+nameGroup+HsaProtocolConstants.DELIMETER+
				minNormalPulse+HsaProtocolConstants.DELIMETER+maxNormalPulse+HsaProtocolConstants.DELIMETER+
				serveyPeriod;
		
	}
	public static String VisitToString(Visit visit){
		int idDoctor=visit.getDoctor().getId();
		String nameDoctor=visit.getDoctor().getName();
		String phone=visit.getDoctor().getPhoneNumber();
		String mail=visit.getDoctor().geteMail();
		int id = visit.getPatient().getId();
		String name = visit.getPatient().getName();
		String phoneNumber = visit.getPatient().getPhoneNumber();
		String eMail = visit.getPatient().geteMail();
		String nameGroup = visit.getPatient().getHealthGroup().getGroupName();
		int minNormalPulse = visit.getPatient().getHealthGroup().getMinNormalPulse();
		int maxNormalPulse = visit.getPatient().getHealthGroup().getMaxNormalPulse();
		int serveyPeriod = visit.getPatient().getHealthGroup().getSurveyPeriod();
		LocalDateTime dateTime = visit.getDateTime();
		
		return idDoctor+HsaProtocolConstants.DELIMETER+nameDoctor+HsaProtocolConstants.DELIMETER+
				phone+HsaProtocolConstants.DELIMETER+mail+
				id+HsaProtocolConstants.DELIMETER+name+
				HsaProtocolConstants.DELIMETER+phoneNumber+HsaProtocolConstants.DELIMETER+
				eMail+HsaProtocolConstants.DELIMETER+nameGroup+HsaProtocolConstants.DELIMETER+
				minNormalPulse+HsaProtocolConstants.DELIMETER+maxNormalPulse+HsaProtocolConstants.DELIMETER+
				serveyPeriod+HsaProtocolConstants.DELIMETER+dateTime;
	}
	public static Doctor stringToDoctor(String[] tokens) {
		int id;
		String name;
		String phoneNumber;
		String eMail;
		try {
			id = Integer.parseInt(tokens[0]);
			name = tokens[1];
			phoneNumber = tokens[2];
			eMail = tokens[3];
		} catch (Throwable e) {
			return null;
		}
		return new Doctor(id, name, phoneNumber, eMail);
	}
	public static String DoctorToString(Doctor doctor){
		int id=doctor.getId();
		String name=doctor.getName();
		String phone=doctor.getPhoneNumber();
		String mail=doctor.geteMail();

		return id+HsaProtocolConstants.DELIMETER+name+HsaProtocolConstants.DELIMETER+
				phone+HsaProtocolConstants.DELIMETER+mail;
	}
	public static Patient stringToPatient(String[] tokens){
		Patient patient;
		int id;
		String name;
		String phoneNumber;
		String eMail;
		int idGroup;
		String nameGroup;
		int minNormalPulse;
		int maxNormalPulse;
		int serveyPeriod;
		try {
			id=Integer.parseInt(tokens[0]);
			name=tokens[1];
			phoneNumber=tokens[2];
			eMail=tokens[3];
			idGroup = Integer.parseInt(tokens[4]);
			nameGroup = tokens[5];
			minNormalPulse = Integer.parseInt(tokens[6]);
			maxNormalPulse = Integer.parseInt(tokens[7]);
			serveyPeriod = Integer.parseInt(tokens[8]);
			HealthGroup healthGroup = new HealthGroup(idGroup,nameGroup, minNormalPulse, maxNormalPulse, serveyPeriod);
			patient = new Patient(id, name, phoneNumber, eMail, healthGroup);
		} catch (Throwable e) {
			return null;
		}
		return patient;
	}
}
