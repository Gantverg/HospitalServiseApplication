package tel_ran.hsa.entities.jfx;

import java.time.LocalDateTime;

import javafx.beans.property.*;
import tel_ran.hsa.entities.dto.HeartBeat;

public class HeartBeatJfx {
	IntegerProperty patientId;
	ObjectProperty<LocalDateTime> dateTime;
	IntegerProperty value;
	IntegerProperty surveyPeriod;

	public HeartBeatJfx() {
	}

	public HeartBeatJfx(int patientId, String dateTime, int value, int surveyPeriod) {
		super();
		this.patientId.set(patientId);
		this.dateTime.set(LocalDateTime.parse(dateTime));
		this.value.set(value);
		this.surveyPeriod.set(surveyPeriod);
	}
	
	public HeartBeatJfx(HeartBeat heartBeat) {
		this(heartBeat.getPatientId(), heartBeat.getDateTime().toString(), heartBeat.getValue(), heartBeat.getSurveyPeriod());
	}

	public int getPatientId() {
		return patientId.get();
	}

	public LocalDateTime getDateTime() {
		return dateTime.get();
	}

	public int getValue() {
		return value.get();
	}

	public int getSurveyPeriod() {
		return surveyPeriod.get();
	}

	public void setPatientId(int patientId) {
		this.patientId.set(patientId);
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime.set(dateTime);
	}

	public void setValue(int value) {
		this.value.set(value);
	}

	public void setSurveyPeriod(int surveyPeriod) {
		this.surveyPeriod.set(surveyPeriod);
	}

	public IntegerProperty patientId() {
		return this.patientId;
	}

	public ObjectProperty<LocalDateTime> dateTime() {
		return this.dateTime;
	}

	public IntegerProperty value() {
		return this.value;
	}

	public IntegerProperty surveyPeriod() {
		return this.surveyPeriod;
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
		result = prime * result + patientId.get();
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
		HeartBeatJfx other = (HeartBeatJfx) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.get().equals(other.dateTime.get()))
			return false;
		if (patientId.get() != other.patientId.get())
			return false;
		return true;
	}

}
