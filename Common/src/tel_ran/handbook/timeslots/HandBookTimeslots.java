package tel_ran.handbook.timeslots;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import tel_ran.hsa.entities.dto.TimeSlot;

public class HandBookTimeslots {
	static TimeSlot[] timeslot1 = {new TimeSlot(1, LocalTime.of(8, 0), LocalTime.of(15, 0)),
                            new TimeSlot(3, LocalTime.of(12, 0), LocalTime.of(19, 0)),
                            new TimeSlot(5, LocalTime.of(11, 0), LocalTime.of(17, 0)),
                            new TimeSlot(7, LocalTime.of(8, 0), LocalTime.of(15, 0))};
	
	static TimeSlot[] timeslot2 = {new TimeSlot(2, LocalTime.of(8, 0), LocalTime.of(15, 0)),
							new TimeSlot(4, LocalTime.of(12, 0), LocalTime.of(19, 0)),
							new TimeSlot(6, LocalTime.of(11, 0), LocalTime.of(17, 0))};
	static Map<Integer,TimeSlot[]> timeslots = new HashMap<Integer,TimeSlot[]>(){
	
	{
		put(1,timeslot1);
		put(2, timeslot2);
	};
};
	public static Map<Integer,TimeSlot[]> getTimeSlots()
	{
		return timeslots;
	}
	
	

}
