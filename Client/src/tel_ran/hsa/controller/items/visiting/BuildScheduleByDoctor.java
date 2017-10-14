package tel_ran.hsa.controller.items.visiting;

import java.time.LocalDate;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.entities.dto.Doctor;
import tel_ran.hsa.entities.dto.Visit;
import tel_ran.hsa.tests.model.ScheduleNotEmptyException;

public class BuildScheduleByDoctor extends HospitalItem {
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
		   inputOutput.put(String.format("Doctor with id %d doesn`t exist",doctorId));
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
