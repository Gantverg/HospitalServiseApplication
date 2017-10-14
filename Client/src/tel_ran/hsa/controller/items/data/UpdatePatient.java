package tel_ran.hsa.controller.items.data;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.entities.dto.Patient;
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
		String res=hospital.updatePatient(new Patient(patientId, name, phoneNumber, eMail));
		if (res.equals(RestResponseCode.NO_PATIENT))
		{
			inputOutput.put(String.format("Patient with id %d doesn`t exist", patientId));
			return;
		}
		if (res.equals(RestResponseCode.NO_PATIENT)) {
		inputOutput.put(String.format("Patient with id %d  was updated",
		patientId));
		}
	}
}
