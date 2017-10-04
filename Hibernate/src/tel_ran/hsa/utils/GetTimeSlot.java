package tel_ran.hsa.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import tel_ran.hsa.entities.dto.TimeSlot;

public class GetTimeSlot {
	int doctorId;
	TimeSlot[] slots;
	
	public GetTimeSlot() {}
	
	@JsonCreator
	public GetTimeSlot(@JsonProperty("doctorId") int doctorId,
						@JsonProperty("slots") TimeSlot[] slots) {
		this.doctorId=doctorId;
		this.slots=slots;
		
	}

	public int getDoctorId() {
		return doctorId;
	}

	public TimeSlot[] getSlots() {
		return slots;
	}
	
}
