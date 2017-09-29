package tel_ran.hospital.entities;

import java.time.*;
import java.util.*;

public class Doctor  {
	int id;
	String name;
	String phoneNumber;
	String eMail;
	Set<DayOfWeek> workingDays;
	
	public Doctor(int id, String name, String phoneNumber, String eMail) {
		super();
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.eMail = eMail;
	}
	
	public Doctor() {}
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
	public Set<DayOfWeek> getWorkingDays() {
		return workingDays;
	}
	public void setWorkingDays(Set<DayOfWeek> workingDays) {
		this.workingDays = workingDays;
	}

	
}
