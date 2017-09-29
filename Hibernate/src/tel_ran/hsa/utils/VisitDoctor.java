package tel_ran.hsa.utils;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import tel_ran.jackson.*;

public class VisitDoctor {
	private int doctorId;
	private int newdoctorId;
	@JsonSerialize (using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime start;
	@JsonSerialize (using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime end;
	
	@JsonCreator
	public VisitDoctor(@JsonProperty("doctorId") int doctorId,
						@JsonProperty("newdoctorId") int newdoctorId,
						@JsonProperty("start") LocalDateTime start,
						@JsonProperty("end") LocalDateTime end) {
		this.doctorId = doctorId;
		this.newdoctorId=newdoctorId;
		this.start=start;
		this.end = end;
		
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getNewdoctorId() {
		return newdoctorId;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

}
