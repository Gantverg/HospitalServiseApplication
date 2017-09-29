package tel_ran.hsa.controller;

import tel_ran.hsa.entities.dto.*;

public class GetPatientDoctors extends HospitalItem {

	@Override
	public String displayedName() {
		return "Get list of patient doctors";
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
		Iterable<Doctor> doctors = hospital.getPatientDoctors(patientId);
		if (doctors==null)
		{
			inputOutput.put(String.format("Patient with id %s have no doctors",patientId));
			return;
		}
		inputOutput.put(doctors);

		
	}

}
