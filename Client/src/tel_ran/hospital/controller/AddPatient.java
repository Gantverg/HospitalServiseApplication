package tel_ran.hospital.controller;

//import tel_ran.hospital.entities.Doctor;
//import tel_ran.hospital.entities.HealthGroup;
import tel_ran.hospital.entities.HealthGroupCollection;
import tel_ran.hsa.model.dto.*;
//import tel_ran.hospital.entities.Patient;
//import tel_ran.hospital.protocols.RestResponseCode;
import tel_ran.hsa.protocols.api.RestResponseCode;

public class AddPatient extends HospitalItem {
	@Override
	public String displayedName() {
		return "Add patient";
	}

	@Override
	public void perform() {
		Integer patientId=inputOutput.getInteger("Enter patient id");
		String name=inputOutput.getString("Enter patient name");
		String phoneNumber=inputOutput.getString("Enter patient phone number");
		String eMail=inputOutput.getString("Enter patient email");
		HealthGroupCollection.showListHealthGroupsNames();
		Integer groupId = inputOutput.getInteger("Enter patient group number");
		//String[] keys = (String[]) HealthGroupCollection.getHealthgroups().keySet().toArray();
		String res=hospital.addPatient(new Patient(patientId, name, phoneNumber, eMail,HealthGroupCollection.getHealthgroups().get(groupId)));
		if (res==RestResponseCode.ALREADY_EXIST)
		{
			inputOutput.put(String.format("Patient with id %d already exist", patientId));
			return;
		}
		if (res==RestResponseCode.OK) {
		inputOutput.put(String.format("Patient with id %d was added",
		patientId));
		}
	}

}
