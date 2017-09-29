package tel_ran.hsa.entities.orm;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
 
@Entity
public class HealthGroupOrm {
	@Id
	int id;
	String name;
	int minNormalPulse;
	int maxNormalPulse;
	int serveyPeriod;
	@OneToMany(mappedBy="healthGroupOrm")
	Set<PatientOrm> patients;

	public HealthGroupOrm() {}
	
	public HealthGroupOrm(int id, String name, int minNormalPulse, int maxNormalPulse, int serveyPeriod) {
		this.id = id;
		this.name = name;
		this.minNormalPulse = minNormalPulse;
		this.maxNormalPulse = maxNormalPulse;
		this.serveyPeriod = serveyPeriod;
	}


	public String getName() {
		return name;
	}
	public int getMinNormalPulse() {
		return minNormalPulse;
	}
	public int getMaxNormalPulse() {
		return maxNormalPulse;
	}
	public int getServeyPeriod() {
		return serveyPeriod;
	}

	public Set<PatientOrm> getPatient() {
		return patients;
	}
	
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "HealthGroupOrm [id=" + id + ", name=" + name + ", minNormalPulse=" + minNormalPulse
				+ ", maxNormalPulse=" + maxNormalPulse + ", serveyPeriod=" + serveyPeriod 
				+ "]";
	}
	
	
}
