package tel_ran.hsa.controller.items.data;

import java.util.Arrays;
import java.util.Map;

import tel_ran.handbook.timeslots.HandBookTimeslots;
import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.entities.dto.TimeSlot;
import tel_ran.hsa.protocols.api.RestResponseCode;

public class SetTimeSlot extends HospitalItem {

	@Override
	public String displayedName() {
		// TODO Auto-generated method stub
		return "Set timeslot";
	}

	@Override
	public void perform() {
	int doctorId = inputOutput.getInteger(String.format("Set doctor id for set tomeslots"));
	 Map<Integer,TimeSlot[]> timeslots = HandBookTimeslots.getTimeSlots();	
	inputOutput.put("List of timeslots:");
	for (Map.Entry<Integer,TimeSlot[]> pair : timeslots.entrySet())
	{
		inputOutput.put(String.format("%d. %s", pair.getKey(),Arrays.toString(pair.getValue())));
	}
    int id = inputOutput.getInteger(String.format("Choose timeslot in range 1 - %d:",timeslots.keySet().size()));
    String res = hospital.setTimeSlot(doctorId, timeslots.get(id));
    if (res.equals(RestResponseCode.NO_DOCTOR))
    { 
    	inputOutput.put(String.format("Doctor with id %d  doesn`t exist",doctorId));
    }
    
    if (res.equals(RestResponseCode.OK))
    { 
    	inputOutput.put(String.format("Set timeslots for Doctor with id %d",doctorId));
    }
    
	}

}
