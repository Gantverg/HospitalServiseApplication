package tel_ran.hsa.entities.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import tel_ran.jackson.LocalDateTimeDeserializer;
import tel_ran.jackson.LocalDateTimeSerializer;

public class Visit {
	Doctor doctor;
	Patient patient;
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	LocalDateTime dateTime;
	boolean blocked;

	public Visit() {
	}

	public Visit(Doctor doctor, Patient patient, LocalDateTime dateTime) {
		super();
		this.doctor = doctor;
		this.patient = patient;
		this.dateTime = dateTime;
		blocked = false;
	}

	@Override
	public String toString() {
		return "Visit [dateTime=" + dateTime + ", doctor=" + doctor + ", patient=" + patient + "]";
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public Patient getPatient() {
		return patient;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + ((doctor == null) ? 0 : doctor.hashCode());
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
		Visit other = (Visit) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (doctor == null) {
			if (other.doctor != null)
				return false;
		} else if (!doctor.equals(other.doctor))
			return false;
		return true;
	}

}
