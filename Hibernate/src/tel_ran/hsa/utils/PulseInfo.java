package tel_ran.hsa.utils;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import tel_ran.jackson.*;

public class PulseInfo {
	private int patientId;
	private int value;
	@JsonSerialize (using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime dateTime;
	
	@JsonCreator
	public PulseInfo(@JsonProperty("patientId") int patientId,
					@JsonProperty("value") int value,
					@JsonProperty("dateTime") LocalDateTime dateTime) {
		this.patientId=patientId;
		this.value=value;
		this.dateTime=dateTime;
	}

	public int getPatientId() {
		return patientId;
	}

	public int getValue() {
		return value;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
}
