package tel_ran.hsa.controller;

import java.time.LocalDate;
import tel_ran.hsa.entities.dto.*;

public class GetFreeVisits extends HospitalItem {
	@Override
	public String displayedName() {
		return "Get visits by doctor";
	}

	@Override
	public void perform() {
		
		Integer doctorId=inputOutput.getInteger("Enter doctor id");
		LocalDate beginDate=inputOutput.getDate
				("Enter begin date in the format "+format, format);
		LocalDate endDate=inputOutput.getDate
				("Enter end date in the format "+format, format);
        
		Doctor doctor = hospital.getDoctor(doctorId);
		if (doctor==null)
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t exist ", doctorId));
		return;
		}
		Iterable<Visit> res = hospital.getVisitsByDoctor(doctorId, beginDate, endDate);
		if (res==null)
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t have visits from %t1 until %t2 ", doctorId,beginDate.toString(),endDate.toString()));
		return;
		}
		
		inputOutput.put(res);
	}

}
