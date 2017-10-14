package tel_ran.hsa.controller.items.reports;

import java.time.LocalDate;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.entities.dto.*;

public class GetPulseByPeriod extends HospitalItem {

	@Override
	public String displayedName() {
		return "Get patient pulse by period";
	}

	@Override
	public void perform() {
		Integer patientId=inputOutput.getInteger("Enter patient id");
		Patient patient = hospital.getPatient(patientId);
		if (patient==null)
		{
			inputOutput.put(String.format("Patient with id %s doesn`t exist"));
			return;
		}
		LocalDate beginDate=inputOutput.getDate
				("Enter begin date in the format "+format, format);
		LocalDate endDate=inputOutput.getDate
				("Enter end date in the format "+format, format);
		Integer surveyPeriod=inputOutput.getInteger("Enter patient surveyPeriod");
		Iterable<Integer> res = hospital.getPulseByPeriod(patientId,beginDate,endDate,surveyPeriod);
		
		
		if (res==null)
		{
			inputOutput.put(String.format("Patient with id %d have no pulse information from %s until %s",patientId,beginDate.toString(),endDate.toString()));
			return;
		}
		inputOutput.put(res);

		
	}

}
