package tel_ran.hospital.entitiesORM;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class DoctorOrm  {
	@Id
	protected int id;
	protected String name;
	protected String phoneNumber;
	protected String eMail;
	
	@OneToMany(mappedBy="doctors")
	Set<VisitOrm> visits;
	
	


	public DoctorOrm(int id, String name, String phoneNumber, String eMail) {
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.eMail = eMail;
		//this.visits = visits;
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
				+ ", visits=" + visits + "]";
	}


}