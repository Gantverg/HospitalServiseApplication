package tel_ran.hsa.entities.orm;

import java.util.Set;

import javax.persistence.*;

import tel_ran.hsa.entities.dto.HealthGroup;
 
@Entity
public class HealthGroupOrm {
	@Id
	int id;
	public void setMinNormalPulse(int minNormalPulse) {
		this.minNormalPulse = minNormalPulse;
	}

	public void setMaxNormalPulse(int maxNormalPulse) {
		this.maxNormalPulse = maxNormalPulse;
	}

	String name;
	int minNormalPulse;
	int maxNormalPulse;
	int surveyPeriod;
	@OneToMany(mappedBy="healthGroupOrm")
	Set<PatientOrm> patients;

	public HealthGroupOrm() {}
	
	public HealthGroupOrm(int id, String name, int minNormalPulse, int maxNormalPulse, int surveyPeriod) {
		this.id = id;
		this.name = name;
		this.minNormalPulse = minNormalPulse;
		this.maxNormalPulse = maxNormalPulse;
		this.surveyPeriod = surveyPeriod;
	}


	public HealthGroup getHealthGroup() {
		return new HealthGroup(id, name, minNormalPulse, maxNormalPulse, surveyPeriod);
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
	public int getSurveyPeriod() {
		return surveyPeriod;
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
				+ ", maxNormalPulse=" + maxNormalPulse + ", surveyPeriod=" + surveyPeriod 
				+ "]";
	}
	
	
}
