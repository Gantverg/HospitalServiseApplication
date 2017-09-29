package tel_ran.hsa.utils;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import tel_ran.jackson.*;

public class BookVisit {
	private int doctorId;
	private int patientId;
	@JsonSerialize (using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime dateTime;
	
	 @JsonCreator
	 public BookVisit(@JsonProperty("doctorId") int doctorId,
			 @JsonProperty("patientId") int patientId,
			 @JsonProperty("dateTime") LocalDateTime dateTime) {
		 this.doctorId = doctorId;
		 this.patientId = patientId;
		 this.dateTime = dateTime;
	 }

	public int getDoctorId() {
		return doctorId;
	}

	public int getPatientId() {
		return patientId;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
	 
	 
}
