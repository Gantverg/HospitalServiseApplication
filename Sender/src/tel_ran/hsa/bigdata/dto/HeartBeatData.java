package tel_ran.hsa.bigdata.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import tel_ran.hsa.entities.dto.Patient;
import tel_ran.jackson.LocalDateTimeDeserializer;

public class HeartBeatData {
	Patient patient;
	public Patient getPatient() {
		return patient;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
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
	public HeartBeatData(Patient patient, LocalDateTime dateTime, int value, int surveyPeriod) {
		super();
		this.patient = patient;
		this.dateTime = dateTime;
		this.value = value;
		this.surveyPeriod = surveyPeriod;
	}
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	LocalDateTime dateTime;
	int value;
	int surveyPeriod;
}
