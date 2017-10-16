package tel_ran.hsa.entities.jfx;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import tel_ran.hsa.entities.dto.Doctor;
import tel_ran.hsa.entities.dto.TimeSlot;

public class DoctorJfx extends PersonJfx {
	ObservableSet<TimeSlot> timeSlots = FXCollections.emptyObservableSet();
	BooleanProperty dismissed;

	public DoctorJfx() {
		this.timeSlots = FXCollections.emptyObservableSet();
	}
	
	public DoctorJfx(int id, String name, String phoneNumber, String eMail) {
		super(id, name, phoneNumber, eMail);
	}
	
	public DoctorJfx(int id, String name, String phoneNumber, String eMail, Set<TimeSlot> timeSlots) {
		super(id, name, phoneNumber, eMail);
		setTimeSlots(timeSlots);
	}
	
	public DoctorJfx(Doctor doctor) {
		this(doctor.getId(), doctor.getName(), doctor.getPhoneNumber(), doctor.geteMail(), doctor.getTimeSlots());
	}
	
	public Doctor get() {
		
		return new Doctor(id.get(), name.get(), phoneNumber.get(), eMail.get(), getTimeSlots());
	}
	
	public boolean isDismissed() {
		return dismissed.get();
	}

	public void setDismissed(boolean dismissed) {
		this.dismissed.set(dismissed);
	}
	
	public BooleanProperty dismissedProperty() {
		return this.dismissed;
	}

	public Set<TimeSlot> getTimeSlots() {
		return timeSlots.stream().collect(Collectors.toSet());
	}
	
	public void setTimeSlots(Set<TimeSlot> timeSlotsSet) {
		timeSlots = FXCollections.observableSet(timeSlotsSet);
	}
	
	public ObservableSet<TimeSlot> timeSlotsProperty() {
		return this.timeSlots;
	}

/*	public boolean isDayWorking(LocalDate date) {
		for (TimeSlotJfx timeSlot : getTimeSlots()) {
			if(timeSlot.isDateInSlot(date))
				return true;
		}
		return false;
	}
	public boolean isDayTimeWorking(LocalDateTime dateTime) {
		for (TimeSlotJfx timeSlot : getTimeSlots()) {
			if(timeSlot.isDateTimeInSlot(dateTime))
				return true;
		}
		return false;
	}
*/}
