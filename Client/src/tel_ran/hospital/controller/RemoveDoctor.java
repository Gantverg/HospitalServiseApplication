package tel_ran.hospital.controller;

import tel_ran.hospital.entities.not_use.Doctor;
import tel_ran.hospital.protocols.not_use.RestResponseCode;

public class RemoveDoctor extends HospitalItem {
	@Override
	public String displayedName() {
		return "Remove a doctor";
	}

	@Override
	public void perform() {
		Integer doctorId=inputOutput.getInteger("Enter doctor id for removing");
		String res=hospital.removeDoctor(doctorId);
		if (res==RestResponseCode.NO_DOCTOR)
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t exist", doctorId));
			return;
		}
		if (res==RestResponseCode.OK) {
		inputOutput.put(String.format("Doctor with id %d was removed",
		doctorId));
		}
	}

}
