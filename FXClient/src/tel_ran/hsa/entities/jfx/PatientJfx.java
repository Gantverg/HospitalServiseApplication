package tel_ran.hsa.entities.jfx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import tel_ran.hsa.entities.dto.Doctor;
import tel_ran.hsa.entities.dto.HealthGroup;
import tel_ran.hsa.entities.dto.Patient;

public class PatientJfx extends PersonJfx {
	ObjectProperty<HealthGroup> healthGroup;
	ObjectProperty<Doctor> therapist;

	public PatientJfx() {
	}

	public PatientJfx(int id, String name, String phoneNumber, String eMail) {
		super(id, name, phoneNumber, eMail);
	}

	public PatientJfx(int id, String name, String phoneNumber, String eMail, HealthGroup healthGroup, Doctor therapist) {
		super(id, name, phoneNumber, eMail);
		this.healthGroup = new SimpleObjectProperty<HealthGroup>(healthGroup);
		this.therapist = new SimpleObjectProperty<Doctor>(therapist);
	}
	
	public PatientJfx(Patient patient) {
		this(patient.getId(), 
				patient.getName(), 
				patient.getPhoneNumber(), 
				patient.geteMail(), 
				patient.getHealthGroup(), 
				patient.getTherapist());
	}

	public Patient get() {
		return new Patient(id.get(), name.get(), phoneNumber.get(), eMail.get(), healthGroup.get(), therapist.get());
	}
	
	public HealthGroup getHealthGroup() {
		return healthGroup.get();
	}

	public void setHealthGroup(HealthGroup healthGroup) {
		this.healthGroup.set(healthGroup);
	}
	
	public ObjectProperty<HealthGroup> healthGroupProperty() {
		return healthGroup;
	}

	public Doctor getTherapist() {
		return therapist.get();
	}

	public void setTherapist(Doctor therapist) {
		this.therapist.set(therapist);
	}
	
	public ObjectProperty<Doctor> therapistProperty() {
		return therapist;
	}

}
