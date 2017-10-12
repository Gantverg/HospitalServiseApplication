package tel_ran.hsa.bigdata.dto;


import tel_ran.hsa.entities.dto.Patient;

public class HeartBeatData {
	String patientName;
	String doctorName;
	String doctorMail;
	int value;
	int surveyPeriod;
	public String getPatientName() {
		return patientName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public String getDoctorMail() {
		return doctorMail;
	}
	public int getValue() {
		return value;
	}
	public int getSurveyPeriod() {
		return surveyPeriod;
	}
	public HeartBeatData() {
		super();
	}
	public HeartBeatData(String patientName, String doctorName, String doctorMail, int value, int surveyPeriod) {
		super();
		this.patientName = patientName;
		this.doctorName = doctorName;
		this.doctorMail = doctorMail;
		this.value = value;
		this.surveyPeriod = surveyPeriod;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public void setDoctorMail(String doctorMail) {
		this.doctorMail = doctorMail;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public void setSurveyPeriod(int surveyPeriod) {
		this.surveyPeriod = surveyPeriod;
	}
	
}
