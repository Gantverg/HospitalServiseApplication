package tel_ran.hospital.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import tel_ran.hsa.model.dto.*;

//import tel_ran.hospital.entities.not_use.Doctor;
//import tel_ran.hospital.entities.not_use.Patient;
//import tel_ran.hospital.entities.not_use.Visit;
//import tel_ran.hospital.protocols.not_use.RestResponseCode;

public class GetVisitsByDoctor extends HospitalItem {
	@Override
	public String displayedName() {
		return "Get free visits by doctor";
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
		Iterable<Visit> res = hospital.getFreeVisits(doctorId, beginDate, endDate);
		if (res==null)
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t have free visits from %t1 until %t2 ", doctorId,beginDate.toString(),endDate.toString()));
		return;
		}
		
		inputOutput.put(res);
	}

}
