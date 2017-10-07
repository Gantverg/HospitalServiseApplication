package tel_ran.hsa.controller;

import java.time.LocalDate;

import tel_ran.view.Item;

public class GetPulseBySurveyPeriod extends HospitalItem {

	@Override
	public String displayedName() {
		// TODO Auto-generated method stub
		return "Get pulse by survey period";
	}

	@Override
	public void perform() {
		int patientId = inputOutput.getInteger("Enter patient id");
		LocalDate beginDate =inputOutput.getDate("Enter begin date of visit"+format,format);
		LocalDate endDate =inputOutput.getDate("Enter end date of visit"+format,format);
		int surveyPeriod= inputOutput.getInteger("Enter survey period");
        Iterable<Integer> res = hospital.getPulseByPeriod(patientId, beginDate, endDate, surveyPeriod);
        if (res==null)
        {
        	inputOutput.put(String.format("Pulse data for patient with id %d in range of %d1 - %d2 by survey period %s doesn`t exist",
					  patientId,beginDate,endDate,surveyPeriod));
					  return;
        }
        inputOutput.put(res);
	}

}
