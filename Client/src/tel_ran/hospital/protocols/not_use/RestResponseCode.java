package tel_ran.hospital.protocols.not_use;

public interface RestResponseCode {
	String OK = "ok";
	String NO_DOCTOR = "no doctor";
	String NO_PATIENT = "no patient";
	String NO_HEALTH_GROUP ="no healthgroup";
	String NO_SCHEDULE = "no schedule on this time";
	String VISIT_BUSY = "doctor at this time is busy";
	String ALREADY_EXIST = "already exist";
}