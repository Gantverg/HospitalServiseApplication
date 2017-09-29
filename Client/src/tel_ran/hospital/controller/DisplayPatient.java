package tel_ran.hospital.controller;

import tel_ran.hsa.model.dto.Patient;

//import tel_ran.hospital.entities.not_use.Doctor;
//import tel_ran.hospital.entities.not_use.Patient;

public class DisplayPatient extends HospitalItem {
	@Override
	public String displayedName() {
		return "Display a patient data";
	}

	@Override
	public void perform() {
		Integer patientId=inputOutput.getInteger("Enter patient id for search");
		Patient patient = hospital.getPatient(patientId);
		if (patient==null) { 
			inputOutput.put(String.format("Patient with id %d doesn`t exist", patientId));
			return;
		}
		inputOutput.put(patient);
	}

}
