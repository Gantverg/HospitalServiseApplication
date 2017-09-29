package tel_ran.hsa.controller;

import tel_ran.hsa.entities.dto.Doctor;
import tel_ran.hsa.protocols.api.RestResponseCode;

public class UpdatePatient extends HospitalItem {
	@Override
	public String displayedName() {
		return "Update a patient information";
	}

	@Override
	public void perform() {
		Integer patientId=inputOutput.getInteger("Enter patient id for update");
		String name=inputOutput.getString("Enter new patient name");
		String phoneNumber=inputOutput.getString("Enter new patient phone number");
		String eMail=inputOutput.getString("Enter new  patient email");
		String res=hospital.updateDoctor(new Doctor(patientId, name, phoneNumber, eMail));
		if (res==RestResponseCode.NO_PATIENT)
		{
			inputOutput.put(String.format("Patient with id %d doesn`t exist", patientId));
			return;
		}
		if (res==RestResponseCode.NO_PATIENT) {
		inputOutput.put(String.format("Patient with id %d %s was updated",
		patientId));
		}
	}
}
