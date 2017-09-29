package tel_ran.hospital.entitiesORM;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class HertBeatOrm {
	@Id
	@GeneratedValue
	int idBeat;
	LocalDateTime dateTime;
	int value;
	int surveyPeriod;
	
	@ManyToOne
	PatientOrm patientPuls;
	
	public HertBeatOrm(PatientOrm patientPuls,LocalDateTime dateTime, int value, int surveyPeriod) {
		this.patientPuls = patientPuls;
		this.dateTime = dateTime;
		this.value = value;
		this.surveyPeriod = surveyPeriod;
	}
	public HertBeatOrm() {}
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public int getValue() {
		return value;
	}
	public int getSurveyPeriod() {
		return surveyPeriod;
	}
	
	public PatientOrm getPatient() {
		return patientPuls;
	}
	@Override
	public String toString() {
		return "HertBeatOrm [dateTime=" + dateTime + ", value=" + value + ", surveyPeriod=" + surveyPeriod
				+ ", patient=" + patientPuls + "]";
	}

	
	
}
