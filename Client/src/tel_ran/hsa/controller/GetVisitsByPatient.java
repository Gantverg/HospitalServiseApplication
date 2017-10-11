package tel_ran.hsa.controller;

import java.time.LocalDate;
import tel_ran.hsa.entities.dto.*;


public class GetVisitsByPatient extends HospitalItem {
	@Override
	public String displayedName() {
		return "Get visits by patient";
	}

	@Override
	public void perform() {
		
		Integer patientId=inputOutput.getInteger("Enter patient id");
		LocalDate beginDate=inputOutput.getDate
				("Enter begin date in the format "+format, format);
		LocalDate endDate=inputOutput.getDate
				("Enter end date in the format "+format, format);
        
		Patient patient = hospital.getPatient(patientId);
		if (patient==null)
		{
			inputOutput.put(String.format("Patient with id %d doesn`t exist ", patientId));
		return;
		}
		Iterable<Visit> res = hospital.getVisitsByPatient(patientId, beginDate, endDate);
		if (res==null)
		{
			inputOutput.put(String.format("Patient with id %d doesn`t have visits from %s until %s ", patientId,beginDate.toString(),endDate.toString()));
		return;
		}
		
		inputOutput.put(res);
	}

}
