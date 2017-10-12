package tel_ran.hsa.controller;

import java.time.LocalDate;

import tel_ran.hsa.entities.dto.Doctor;
import tel_ran.hsa.entities.dto.Visit;
import tel_ran.hsa.tests.model.ScheduleNotEmptyException;

//import tel_ran.hospital.entities.not_use.Doctor;
//import tel_ran.hospital.entities.not_use.Visit;
//import tel_ran.hospital.protocols.not_use.RestResponseCode;

public class BuildSheduleByDoctor extends HospitalItem {
	@Override
	public String displayedName() {
		return "Build schedule by dates and by doctor";
	}

	@Override
	public void perform() {
		Integer doctorId=inputOutput.getInteger("Enter doctor id");
		  Doctor doctor = hospital.getDoctor(doctorId);
		  if (doctor==null)
		  {
		   inputOutput.put(String.format("Doctor with id %s doesn`t exist"));
		   return;
		  }
		LocalDate startDate=inputOutput.getDate
				("Enter start date in the format "+format, format);
		LocalDate finishDate=inputOutput.getDate
				("Enter finish date in the format "+format, format);
		Iterable<Visit> visits;
		try {
			visits = hospital.buildScheduleByDoctor(startDate, finishDate, doctorId);
			if (visits==null)
			{
				inputOutput.put(String.format("Visits were not built"));
			}
			inputOutput.put(String.format("Visits were built"));
		} catch (ScheduleNotEmptyException e) {
			inputOutput.put(String.format("Visits were not built"));
		}
	}

}
