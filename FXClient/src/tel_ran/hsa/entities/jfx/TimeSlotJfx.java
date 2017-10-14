package tel_ran.hsa.entities.jfx;

import java.time.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import tel_ran.hsa.entities.dto.TimeSlot;

public class TimeSlotJfx {
	IntegerProperty numberDayOfWeek; 
	ObjectProperty<LocalTime> beginTime;
	ObjectProperty<LocalTime> endTime;

	public TimeSlotJfx() {
	}
	
	public TimeSlotJfx(int numberDayOfWeek, LocalTime beginTime, LocalTime endTime) {
		super();
		this.numberDayOfWeek.set(numberDayOfWeek);
		this.beginTime.set(beginTime);
		this.endTime.set(endTime);
	}

	public TimeSlotJfx(TimeSlot timeSlot) {
		this(timeSlot.getNumberDayOfWeek(), timeSlot.getBeginTime(), timeSlot.getEndTime());
	}
	
	public TimeSlot get() {
		return new TimeSlot(numberDayOfWeek.get(), beginTime.get(), endTime.get());
	}

	public int getNumberDayOfWeek() {
		return numberDayOfWeek.get();
	}

	public LocalTime getBeginTime() {
		return beginTime.get();
	}

	public LocalTime getEndTime() {
		return endTime.get();
	}
	
	public IntegerProperty numberDayOfWeekProperty() {
		return numberDayOfWeek;
	}
	
	public ObjectProperty<LocalTime> beginTimeProperty() {
		return beginTime;
	}

	public ObjectProperty<LocalTime> endTimeProperty() {
		return beginTime;
	}

	@Override
	public String toString() {
		return "TimeSlot [" + getDayOfWeek(numberDayOfWeek.get()) + " time=" + beginTime + " - " + endTime
				+ "]";
	}

	private String getDayOfWeek(int dayOfWeek) {
		return DayOfWeek.of(dayOfWeek).name();
	}

	public boolean isDateTimeInSlot(LocalDateTime dateTime) {
		int checkDayOfWeek = dateTime.getDayOfWeek().getValue();
		LocalTime checkTime = dateTime.toLocalTime(); 
		return ((checkDayOfWeek == this.numberDayOfWeek.get()&&
			  (checkTime.isAfter(beginTime.get())||checkTime.equals(beginTime.get()))&&
			  checkTime.isBefore(endTime.get())));
	}
	
	public boolean isDateInSlot(LocalDate date) {
		return date.getDayOfWeek().getValue() == numberDayOfWeek.get();
		
	}

}
