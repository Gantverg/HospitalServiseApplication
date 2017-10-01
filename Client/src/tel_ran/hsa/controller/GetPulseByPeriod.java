package tel_ran.hsa.controller;

import java.time.LocalDate;

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
			inputOutput.put(String.format("Patient with id %s have no pulse information from %t1 until %t2",patientId,beginDate.toString(),endDate.toString()));
			return;
		}
		inputOutput.put(res);

		
	}

}
