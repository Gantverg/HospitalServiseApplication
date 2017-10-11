package tel_ran.hsa.controller;

import java.util.Map;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.entities.util.HealthGroupCollection;
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
		//HealthGroupCollection.showListHealthGroupsNames();
		Map<Integer, HealthGroup> listHealthGroups = HealthGroupCollection.getHealthgroups();
		//listHealthGroups.entrySet().forEach(pair->inputOutput.put(String.format("%d.%s",pair.getKey(),pair.getValue().getGroupName())));
		//Integer groupId = inputOutput.getInteger("Enter patient group number");
		//String[] keys = (String[]) HealthGroupCollection.getHealthgroups().keySet().toArray();
		String res=hospital.addPatient(new Patient(patientId, name, phoneNumber, eMail));
		if (res.equals(RestResponseCode.ALREADY_EXIST))
		{
			inputOutput.put(String.format("Patient with id %d already exist", patientId));
			return;
		}
		if (res.equals(RestResponseCode.OK)) {
		inputOutput.put(String.format("Patient with id %d was added",
		patientId));
		}
	}

}
