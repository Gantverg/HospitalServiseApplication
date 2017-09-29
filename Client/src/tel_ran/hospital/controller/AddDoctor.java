package tel_ran.hospital.controller;

import tel_ran.hospital.protocols.not_use.RestResponseCode;
import tel_ran.hsa.model.dto.Doctor;

public class AddDoctor extends HospitalItem {
	@Override
	public String displayedName() {
		return "Add doctor";
	}

	@Override
	public void perform() {
		Integer doctorId=inputOutput.getInteger("Enter doctor id");
		String name=inputOutput.getString("Enter doctor name");
		String phoneNumber=inputOutput.getString("Enter doctor phone number");
		String eMail=inputOutput.getString("Enter doctor email");
		String res=hospital.addDoctor(new Doctor(doctorId, name, phoneNumber, eMail));
		if (res==RestResponseCode.ALREADY_EXIST)
		{
			inputOutput.put(String.format("Doctor with id %d already exist", doctorId));
			return;
		}
		if (res==RestResponseCode.OK)
		{
		inputOutput.put(String.format("Doctor with id %d was added",
		doctorId));
		}
	}

}
