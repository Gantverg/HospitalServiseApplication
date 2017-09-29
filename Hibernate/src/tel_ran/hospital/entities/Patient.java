package tel_ran.hospital.entities;

public class Patient {
	int id;
	String name;
	String phoneNumber;
	String eMail;
	HealthGroup healthGroup;
	public Patient(int id, String name, String phoneNumber, String eMail, HealthGroup healthGroup) {
		super();
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.eMail = eMail;
		this.healthGroup = healthGroup;
	}
public Patient() {}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPhoneNumber() {
	return phoneNumber;
}
public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
}
public String geteMail() {
	return eMail;
}
public void seteMail(String eMail) {
	this.eMail = eMail;
}
public HealthGroup getHealthGroup() {
	return healthGroup;
}
public void setHealthGroup(HealthGroup healthGroup) {
	this.healthGroup = healthGroup;
}

	
	
}
