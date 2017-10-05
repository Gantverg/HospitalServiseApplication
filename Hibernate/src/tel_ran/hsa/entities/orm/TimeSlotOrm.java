package tel_ran.hsa.entities.orm;

import java.time.LocalTime;

import javax.persistence.*;

@Entity
public class TimeSlotOrm {
	@Id
	@GeneratedValue
	int id;
	int numberDayOfWeek; 
	LocalTime beginTime;
	LocalTime endTime;
	@ManyToOne(cascade=CascadeType.ALL)
	DoctorOrm doctorSlot;
	
	public TimeSlotOrm() {
	}

	public TimeSlotOrm(int numberDayOfWeek, LocalTime beginTime, LocalTime endTime) {
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
		return "TimeSlotOrm [numberDayOfWeek=" + numberDayOfWeek + ", beginTime=" + beginTime + ", endTime=" + endTime
				+ "]";
	}
	
}
