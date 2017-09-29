package tel_ran.hsa.protocols.api;

public interface RestRequest {
	String WORKINGDAYS_ADD = "/workingdays/add";
	String WORKINGDAYS_REMOVE = "/workingdays/remove";
	String WORKINGDAYS_GET = "/workingdays/get";
	
	String DOCTOR_ADD = "/doctor/add";
	String DOCTOR_REMOVE = "/doctor/remove";
	String DOCTOR_GET = "/doctor/get";
	String DOCTOR_UPDATE = "/doctor/update";
	String DOCTOR_WORKINGDAYS_SET = "/doctor/workingdays/set";
	
	String HEALTHGROUP_ADD = "/healthgroup/add";
	String HEALTHGROUP_REMOVE = "/healthgroup/remove";
	String HEALTHGROUP_GET = "/healthgroup/get";
	
	String PATIENT_ADD = "/patient/add";
	String PATIENT_REMOVE = "/patient/remove";
	String PATIENT_GET = "/patient/get";
	String PATIENT_UPDATE = "/patient/update";
	String PATIENT_HEALTHGROUP_SET = "/patient/healthgroup/set";
	
	String HEALTHGROUPS_GET = "/healthgroups/get";
	String DOCTORS_GET = "/doctors/get";
	String PATIENTS_GET = "/patients/get";

	String VISIT_BOOK = "/visit/book";
	String VISIT_CANCEL = "/visit/cancel";
	
	String SCHEDULE_BUILD = "/schedule/build";
	String SCHEDULE_GET = "/schedule/get";
	
	String VISITS_GET_FREE = "/visits/get/doctor/free";
	String VISITS_GET_DOCTOR = "/visits/get/doctor";
	String VISITS_GET_PATIENT = "/visits/get/patient";
	String VISITS_REPLACE_DOCTOR = "/visits/cancel/doctor";

	String DOCTORS_PATIENT_GET = "/doctors/patient/get";
	String PATIENTS_DOCTOR_GET = "/patients/doctor/get";
	
	String PULSE_ADD = "/pulse/add";
	String PULSE_GET = "/pulse/get";
	
}
