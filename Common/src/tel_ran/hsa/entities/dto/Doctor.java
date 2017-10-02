package tel_ran.hsa.entities.dto;

import java.time.*;
import java.util.Set;

public class Doctor extends Person {
	//@JsonSerialize(using=WorkingDaysSerializer.class)
	//@JsonDeserialize(using=WorkingDaysDeserializer.class)
	Set<TimeSlot> timeSlots;

	public Doctor() {
	}
	
	public Doctor(int id, String name, String phoneNumber, String eMail) {
		super(id, name, phoneNumber, eMail);
	}
	
	public Doctor(int id, String name, String phoneNumber, String eMail, Set<TimeSlot> timeSlots) {
		super(id, name, phoneNumber, eMail);
		setTimeSlots(timeSlots);
	}
	
	public Set<TimeSlot> getTimeSlots() {
		return timeSlots;
	}
	
	public void setTimeSlots(Set<TimeSlot> timeSlots) {
		this.timeSlots = timeSlots;
	}

	public boolean isDayWorking(LocalDate date) {
		for (TimeSlot timeSlot : timeSlots) {
			if(timeSlot.isDateInSlot(date))
				return true;
		}
		return false;
	}
	public boolean isDayTimeWorking(LocalDateTime dateTime) {
		for (TimeSlot timeSlot : timeSlots) {
			if(timeSlot.isDateTimeInSlot(dateTime))
				return true;
		}
		return false;
	}
}
