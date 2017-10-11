 package tel_ran.hsa.controller;

import tel_ran.hsa.protocols.api.RestResponseCode;

public class RemoveDoctor extends HospitalItem {
	@Override
	public String displayedName() {
		return "Remove a doctor";
	}

	@Override
	public void perform() {
		Integer doctorId=inputOutput.getInteger("Enter doctor id for removing");
		String res=hospital.removeDoctor(doctorId);
		if (res.equals(RestResponseCode.NO_DOCTOR))
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t exist", doctorId));
			return;
		}
		if (res.equals(RestResponseCode.OK)) {
		inputOutput.put(String.format("Doctor with id %d was removed",
		doctorId));
		}
	}

}
