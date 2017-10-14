package tel_ran.hsa.entities.jfx;

import java.time.LocalDateTime;

import javafx.beans.property.*;
import tel_ran.hsa.entities.dto.*;

public class VisitJfx {
	ObjectProperty<Doctor> doctor;
	ObjectProperty<Patient> patient;
	ObjectProperty<LocalDateTime> dateTime;
	BooleanProperty blocked;

	public VisitJfx() {
	}

	public VisitJfx(Doctor doctor, Patient patient, LocalDateTime dateTime) {
		super();
		this.doctor.set(doctor);
		this.patient.set(patient);
		this.dateTime.set(dateTime);
		this.blocked.set(false);
	}
	
	public VisitJfx(Visit visit) {
		this(visit.getDoctor(), visit.getPatient(), visit.getDateTime());
	}
	
	@Override
	public String toString() {
		return "Visit [dateTime=" + dateTime + ", doctor=" + doctor + ", patient=" + patient + "]";
	}

	public Doctor getDoctor() {
		return doctor.get();
	}

	public Patient getPatient() {
		return patient.get();
	}

	public LocalDateTime getDateTime() {
		return dateTime.get();
	}

	public void setPatient(Patient patient) {
		this.patient.set(patient); 
	}

	public void setDoctor(Doctor doctor) {
		this.doctor.set(doctor);
	}

	public boolean isBlocked() {
		return blocked.get();
	}

	public void setBlocked(boolean blocked) {
		this.blocked.set(blocked);
	}
	
	public ObjectProperty<Patient> patientProperty() {
		return this.patient;
	}

	public ObjectProperty<Doctor> doctorProperty() {
		return this.doctor;
	}
	
	public ObjectProperty<LocalDateTime> dateTimeProperty() {
		return this.dateTime;
	}

	public BooleanProperty blockedProperty() {
		return this.blocked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.get().hashCode());
		result = prime * result + ((doctor == null) ? 0 : doctor.get().hashCode());
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
		VisitJfx other = (VisitJfx) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.get().equals(other.dateTime.get()))
			return false;
		if (doctor == null) {
			if (other.doctor != null)
				return false;
		} else if (!doctor.get().equals(other.doctor.get()))
			return false;
		return true;
	}

}
