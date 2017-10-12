package tel_ran.hsa.controller;

import tel_ran.hsa.protocols.api.RestResponseCode;
import tel_ran.view.Item;

public class SetTherapist extends HospitalItem {

	@Override
	public String displayedName() {
		
		return "Set therapist";
	}

	@Override
	public void perform() {
		int patientId = inputOutput.getInteger("Enter patient id");
		int doctorId = inputOutput.getInteger("Enter doctor - therapist id");
		String res = hospital.setTherapist(patientId, doctorId);
		if (res.equals(RestResponseCode.NO_PATIENT))
		{
			inputOutput.put(String.format("Patient with id %d doesn`t exist",
					  patientId));
					  return;
		}

		if (res.equals(RestResponseCode.NO_DOCTOR))
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t exist",
					  doctorId));
					  return;
		}
		
		if (res.equals(RestResponseCode.OK))
		{
			inputOutput.put(String.format("Doctor with id %d was set to patient with id %d as a therapist",doctorId,
					 patientId));
					
		}
	}

}
