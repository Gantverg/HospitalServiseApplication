package tel_ran.hsa.controller;

import tel_ran.hsa.protocols.api.RestResponseCode;

public class RemovePatient extends HospitalItem {
	@Override
	public String displayedName() {
		return "Remove a patient";
	}

	@Override
	public void perform() {
		Integer patientId=inputOutput.getInteger("Enter patient id for removing");
		String res=hospital.removePatient(patientId);
		if (res.equals(RestResponseCode.NO_PATIENT))
		{
			inputOutput.put(String.format("Patient with id %d doesn`t exist", patientId));
			return;
		}
		if (res.equals(RestResponseCode.OK))
		{
		inputOutput.put(String.format("Patient with id %d was removed",
		patientId));
		}
	}
}
