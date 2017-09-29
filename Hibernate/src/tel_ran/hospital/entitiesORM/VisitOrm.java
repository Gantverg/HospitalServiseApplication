package tel_ran.hospital.entitiesORM;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.*;

@Entity
public class VisitOrm {
	@Id
	@GeneratedValue
	int id;
	@ManyToOne(cascade=CascadeType.ALL)
	DoctorOrm doctors;
	@ManyToOne(cascade=CascadeType.ALL)
	PatientOrm patients;
	LocalDateTime dateTime;
	
	public VisitOrm() {}

	public VisitOrm(DoctorOrm doctors, PatientOrm patients, LocalDateTime dateTime) {
		this.doctors = doctors;
		this.patients = patients;
		this.dateTime = dateTime;
	}


	@Override
	public String toString() {
		return "VisitOrm [dateTime=" + dateTime + "]";
	}

	public DoctorOrm getDoctors() {
		return doctors;
	}

	public PatientOrm getPatients() {
		return patients;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setPatients(PatientOrm patients) {
		this.patients = patients;
	}

	public void setDoctors(DoctorOrm doctors) {
		this.doctors = doctors;
	}

	
}
