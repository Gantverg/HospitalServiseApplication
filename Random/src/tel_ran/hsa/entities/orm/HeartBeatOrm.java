package tel_ran.hsa.entities.orm;

import java.time.LocalDateTime;

import javax.persistence.*;

import tel_ran.hsa.entities.dto.*;

@Entity
public class HeartBeatOrm {
	@Id
	@GeneratedValue
	int idBeat;
	LocalDateTime dateTime;
	int value;
	int surveyPeriod;
	
	@ManyToOne
	PatientOrm patientPuls;
	
	public HeartBeatOrm(PatientOrm patientPuls,LocalDateTime dateTime, int value, int surveyPeriod) {
		this.patientPuls = patientPuls;
		this.dateTime = dateTime;
		this.value = value;
		this.surveyPeriod = surveyPeriod;
	}
	public HeartBeatOrm() {}
	
	public HeartBeat getHeartBeat() {
		return new HeartBeat(patientPuls.id, dateTime.toString(), value, surveyPeriod);
	}
	
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
