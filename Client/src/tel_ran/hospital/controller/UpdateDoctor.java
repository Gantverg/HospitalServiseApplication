package tel_ran.hospital.controller;

import tel_ran.hsa.model.dto.Doctor;
import tel_ran.hsa.protocols.api.RestResponseCode;

public class UpdateDoctor extends HospitalItem {
	@Override
	public String displayedName() {
		return "Update a doctor information";
	}

	@Override
	public void perform() {
		Integer doctorId=inputOutput.getInteger("Enter doctor id for update");
		String name=inputOutput.getString("Enter new doctor name");
		String phoneNumber=inputOutput.getString("Enter new doctor phone number");
		String eMail=inputOutput.getString("Enter new  doctor email");
		String res=hospital.updateDoctor(new Doctor(doctorId, name, phoneNumber, eMail));
		if (res==RestResponseCode.NO_DOCTOR)
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t exist", doctorId));
			return;
		}
		if (res==RestResponseCode.OK) {
		inputOutput.put(String.format("Doctor with id %d %s was updated",
		doctorId));
		}
	}

}
