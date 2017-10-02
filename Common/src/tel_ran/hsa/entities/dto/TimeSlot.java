package tel_ran.hsa.entities.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeSlot {
	int numberDayOfWeek; 
	LocalTime beginTime;
	LocalTime endTime;

	public TimeSlot() {
	}
	
	public TimeSlot(int numberDayOfWeek, LocalTime beginTime, LocalTime endTime) {
		super();
		this.numberDayOfWeek = numberDayOfWeek;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}

	public int getNumberDayOfWeek() {
		return numberDayOfWeek;
	}

	public LocalTime getBeginTime() {
		return beginTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	@Override
	public String toString() {
		return "TimeSlot [" + getDayOfWeek(numberDayOfWeek) + " time=" + beginTime + " - " + endTime
				+ "]";
	}

	private String getDayOfWeek(int dayOfWeek) {
		return DayOfWeek.of(dayOfWeek).name();
	}

	public boolean isDateTimeInSlot(LocalDateTime dateTime) {
		int checkDayOfWeek = dateTime.getDayOfWeek().getValue();
		LocalTime checkTime = dateTime.toLocalTime(); 
		return ((checkDayOfWeek == this.numberDayOfWeek&&
			  (checkTime.isAfter(beginTime)||checkTime.equals(beginTime))&&
			  checkTime.isBefore(endTime)));
	}
	
	public boolean isDateInSlot(LocalDate date) {
		return date.getDayOfWeek().getValue() == numberDayOfWeek;
		
	}
	
	
	

}
