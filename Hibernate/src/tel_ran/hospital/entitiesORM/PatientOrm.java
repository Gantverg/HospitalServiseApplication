package tel_ran.hospital.entitiesORM;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.*;

@Entity
public class PatientOrm {
	@Id
	protected int id;
	protected String name;
	protected String phoneNumber;
	protected String eMail;
	@ManyToOne(cascade=CascadeType.ALL)
	HealthGroupOrm healthGroupOrm;
	@OneToMany(mappedBy="patients")
	Set<VisitOrm> visits;
	@OneToMany(mappedBy="patientPuls")
	Set<HertBeatOrm> pulse;
	
	
	

	public PatientOrm(int id, String name, String phoneNumber, String eMail,HealthGroupOrm healthGroupOrm) {
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.eMail = eMail;
		this.healthGroupOrm = healthGroupOrm;
	}

	public PatientOrm() {}

	public HealthGroupOrm getHealthGroup() {
		return healthGroupOrm;
	}

	public void setHealthGroup(HealthGroupOrm healthGroup) {
		this.healthGroupOrm = healthGroup;
	}

	
	public Set<VisitOrm> getVisitsPatient() {
		return visits;
	}
	
	
	public Set<HertBeatOrm> getPulsePatients() {
		return pulse;
	}

	public int getId() {
		return id;
	}


	@Override
	public String toString() {
		return "PatientOrm [id=" + id + ", name=" + name + ", phoneNumber=" + phoneNumber + ", eMail=" + eMail
				+ ", healthGroupOrm=" + healthGroupOrm + "]";
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eMail == null) ? 0 : eMail.hashCode());
		result = prime * result + ((healthGroupOrm == null) ? 0 : healthGroupOrm.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((visits == null) ? 0 : visits.hashCode());
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
		PatientOrm other = (PatientOrm) obj;
		if (eMail == null) {
			if (other.eMail != null)
				return false;
		} else if (!eMail.equals(other.eMail))
			return false;
		if (healthGroupOrm == null) {
			if (other.healthGroupOrm != null)
				return false;
		} else if (!healthGroupOrm.equals(other.healthGroupOrm))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (visits == null) {
			if (other.visits != null)
				return false;
		} else if (!visits.equals(other.visits))
			return false;
		return true;
	}

	
}
