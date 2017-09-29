package tel_ran.hospital.entities.not_use;

public class Patient extends Person{
	HealthGroup healthGroup;

	public Patient(int id, String name, String phoneNumber, String eMail, HealthGroup healthGroup) {
		super(id, name, phoneNumber, eMail);
		this.healthGroup = healthGroup;
	}

	public Patient() {
		
	}

	public HealthGroup getHealthGroup() {
		return healthGroup;
	}

	public void setHealthGroup(HealthGroup healthGroup) {
		this.healthGroup = healthGroup;
	}
	
	
}
