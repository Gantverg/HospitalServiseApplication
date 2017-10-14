package tel_ran.hsa.controller.items.data;

import tel_ran.hsa.controller.items.HospitalItem;
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
		
		if (res.equals(RestResponseCode.USING_OBJECT))
		{
		inputOutput.put(String.format("Patient with id %d couldn`t be removed",
		patientId));
		}
		
		
		if (res.equals(RestResponseCode.OK))
		{
		inputOutput.put(String.format("Patient with id %d was removed",
		patientId));
		}
	}
}
