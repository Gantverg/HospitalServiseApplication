package tel_ran.hospital.api;

public enum TcpRequestNotUse {
	ADD_DOCTOR,
	ADD_PATIENT,
	REMOVE_DOCTOR,
	REMOVE_PATIENT,
	UPDATE_DOCTOR,
	UPDATE_PATIENT,
	GET_DOCTOR,
	GET_PATIENT,
	
	BUILD_SCHEDULE,
	BOOK_VISIT,
	CANCEL_VISIT,
	REPLACE_VISITS_DOCTOR,
	
	GET_PATIENT_DOCTORS,
	GET_DOCTOR_PATIENTS,
	
	GET_VISITS_BY_PATIENT,
	GET_VISITS_BY_DOCTOR,
	GET_FREE_VISITS,
	
	ADD_PULSE_INFO,
	GET_PULSE_BY_PERIOD;
	
}
