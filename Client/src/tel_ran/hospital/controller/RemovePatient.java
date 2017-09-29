package tel_ran.hospital.controller;

import tel_ran.hospital.entities.not_use.Doctor;
import tel_ran.hospital.entities.not_use.Patient;
import tel_ran.hospital.protocols.not_use.RestResponseCode;

public class RemovePatient extends HospitalItem {
	@Override
	public String displayedName() {
		return "Remove a patient";
	}

	@Override
	public void perform() {
		Integer patientId=inputOutput.getInteger("Enter patient id for removing");
		String res=hospital.removePatient(patientId);
		if (res==RestResponseCode.NO_PATIENT)
		{
			inputOutput.put(String.format("Patient with id %d doesn`t exist", patientId));
			return;
		}
		if (res==RestResponseCode.OK)
		{
		inputOutput.put(String.format("Patient with id %d was removed",
		patientId));
		}
	}
}
