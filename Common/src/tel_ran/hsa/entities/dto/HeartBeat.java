package tel_ran.hsa.entities.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import tel_ran.jackson.LocalDateTimeDeserializer;
import tel_ran.jackson.LocalDateTimeSerializer;

public class HeartBeat {
	int patientId;
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	LocalDateTime dateTime;
	int value;
	int surveyPeriod;

	public HeartBeat() {
	}

	public HeartBeat(int patientId, LocalDateTime dateTime, int value, int surveyPeriod) {
		super();
		this.patientId = patientId;
		this.dateTime = dateTime;//LocalDateTime.parse(dateTime);
		this.value = value;
		this.surveyPeriod = surveyPeriod;
	}

	public HeartBeat(int patientId, String dateTime, int value, int surveyPeriod) {
		super();
		this.patientId = patientId;
		this.dateTime = LocalDateTime.parse(dateTime);
		this.value = value;
		this.surveyPeriod = surveyPeriod;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setSurveyPeriod(int surveyPeriod) {
		this.surveyPeriod = surveyPeriod;
	}

	public int getPatientId() {
		return patientId;
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

	@Override
	public String toString() {
		return "HeartBeat [patient id=" + patientId + ", dateTime=" + dateTime + ", value=" + value + ", surveyPeriod="
				+ surveyPeriod + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + patientId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeartBeat other = (HeartBeat) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (patientId != other.patientId)
			return false;
		return true;
	}

}
