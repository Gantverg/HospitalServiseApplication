package tel_ran.hsa.protocols.api;

import java.util.Map;
import java.util.stream.Collectors;

public interface RestRequest {
	String WORKINGDAYS = "/workingdays";
	String DOCTORS = "/doctors";
	String HEALTHGROUPS = "/healthgroups";
	String PATIENTS = "/patients";
	String VISITS = "/visits";
	String PULSE = "/pulse";
	String FREE = "/free";
	String REPLACE = "/replace";
	String TIMESLOT = "/timeslots";
	
	String GROUP_ID = "groupId";
	String DAYS_ID = "daysId";
	String PATIENT_ID = "patientId";
	String DOCTOR_ID = "doctorId";
	
	String BEGIN_DATE = "beginDate";
	String END_DATE = "endDate";
	String DATE_TIME = "dateTime";
	String VALUE = "value";
	String HEARTBEAT = "heartbeat";
	String SURVEY_PERIOD = "surveyPeriod";
	String BEGIN_DATE_TIME = "beginDateTime";
	String END_DATE_TIME = "endDateTime";
	
	default String getParamString(Map<String, String> params) {
		return params.entrySet().stream()
			.map(entry->entry.getKey()+"="+entry.getValue())
			.collect(Collectors.joining("&"));
	}
}
