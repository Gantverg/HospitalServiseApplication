package tel_ran.hospital.controller;

import java.time.LocalDate;

import tel_ran.hsa.model.dto.*;
//import tel_ran.hospital.entities.not_use.Doctor;
//import tel_ran.hospital.entities.not_use.Patient;
//import tel_ran.hospital.entities.not_use.Visit;
import tel_ran.security.accounting.Account;


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
		Iterable<Integer> res = hospital.getPulseByPeriod(patientId, beginDate, endDate);
		
		
		if (res==null)
		{
			inputOutput.put(String.format("Patient with id %s have no pulse information from %t1 until %t2",patientId,beginDate.toString(),endDate.toString()));
			return;
		}
		inputOutput.put(res);

		
	}

}
