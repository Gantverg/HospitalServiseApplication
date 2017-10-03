package tel_ran.hsa.entities.dto;

public class Patient extends Person {
	HealthGroup healthGroup;
	Doctor therapist;

	public Patient() {
	}

	public Patient(int id, String name, String phoneNumber, String eMail) {
		super(id, name, phoneNumber, eMail);
	}

	public HealthGroup getHealthGroup() {
		return healthGroup;
	}

	public void setHealthGroup(HealthGroup healthGroup) {
		this.healthGroup = healthGroup;
	}

	public Doctor getTherapist() {
		return therapist;
	}

	public void setTherapist(Doctor therapist) {
		this.therapist = therapist;
	}

}
