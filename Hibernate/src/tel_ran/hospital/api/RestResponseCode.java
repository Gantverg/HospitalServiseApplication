package tel_ran.hospital.api;

public interface RestResponseCode {
	String OK = "ok";
	String NO_DOCTOR = "no doctor";
	String NO_PATIENT = "no patient";
	String NO_SCHEDULE = "no schedule on this time";
	String VISIT_BUSY = "doctor at this time is busy";
	String ALREADY_EXIST = "already exist";
	String NO_HEALTH_GROUP="no healthgroup";
}