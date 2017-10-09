package tel_ran.hsa.entities.orm;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import tel_ran.hsa.entities.dto.*;

@Entity
public class DoctorOrm  {
	@Id
	protected int id;
	protected String name;
	protected String phoneNumber;
	protected String eMail;
	
	@OneToMany(mappedBy="doctors")
	Set<VisitOrm> visits;
	@OneToMany(mappedBy="doctorSlot")
	Set<TimeSlotOrm> slots;
	


	public DoctorOrm(int id, String name, String phoneNumber, String eMail, Set<VisitOrm> visits,
			Set<TimeSlotOrm> slots) {
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.eMail = eMail;
		this.visits = visits;
		this.slots = slots;
	}


	public Set<TimeSlotOrm> getSlots() {
		return slots;
	}


	public void setSlots(Set<TimeSlotOrm> slots) {
		this.slots = slots;
	}


	public DoctorOrm(int id, String name, String phoneNumber, String eMail) {
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.eMail = eMail;
		//this.visits = visits;
	}
	
	public Doctor getDoctor() {
		Doctor doctor = new Doctor(id, name, phoneNumber, eMail);
		if(slots != null) {
			Set<TimeSlot> timeSlots = slots.stream().map(TimeSlotOrm::getTimeSlot).collect(Collectors.toSet());
			doctor.setTimeSlots(timeSlots);
		}
		return doctor;
	}

	public DoctorOrm() {}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNameDoctor() {
		return name;
	}


	public void setNameDoctor(String nameDoctor) {
		this.name = nameDoctor;
	}


	public String getPhoneNumberByDoctor() {
		return phoneNumber;
	}


	public void setPhoneNumberByDoctor(String phoneNumberByDoctor) {
		this.phoneNumber = phoneNumberByDoctor;
	}


	public String geteMailDoctor() {
		return eMail;
	}


	public void seteMailDoctor(String eMailDoctor) {
		this.eMail = eMailDoctor;
	}


	public Set<VisitOrm> getVisitsDoctors() {
		return visits;
	}


	@Override
	public String toString() {
		return "DoctorOrm [id=" + id + ", name=" + name + ", phoneNumber=" + phoneNumber + ", eMail=" + eMail
				+ ", visits=" + visits + ", slots=" + slots + "]";
	}

}
