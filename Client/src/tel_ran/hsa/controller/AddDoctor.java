package tel_ran.hsa.controller;

import tel_ran.hsa.entities.dto.Doctor;
import tel_ran.hsa.protocols.api.RestResponseCode;

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
        inputOutput.put(res);
		if (res.equals(RestResponseCode.ALREADY_EXIST))
		{
			inputOutput.put(String.format("Doctor with id %d already exist", doctorId));
			return;
		}
		if (res.equals(RestResponseCode.OK))
		{
		inputOutput.put(String.format("Doctor with id %d was added",
		doctorId));
		}
	}

}
