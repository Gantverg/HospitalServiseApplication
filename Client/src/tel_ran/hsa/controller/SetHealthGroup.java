package tel_ran.hsa.controller;

import tel_ran.hsa.protocols.api.RestResponseCode;
import tel_ran.view.Item;

public class SetHealthGroup extends HospitalItem {

	@Override
	public String displayedName() {
		
		return "Set healthgroup";
	}

	@Override
	public void perform() {
		int patientId = inputOutput.getInteger("Enter patient id");
		int groupId = inputOutput.getInteger("Enter healthgroup id");
		String res = hospital.setHealthGroup(patientId, groupId);
		if (res==RestResponseCode.NO_PATIENT)
		{
			inputOutput.put(String.format("Patient with id %d doesn`t exist",
					  patientId));
					  return;
		}

		if (res==RestResponseCode.NO_HEALTH_GROUP)
		{
			inputOutput.put(String.format("Healthgroup with id %d doesn`t exist",
					  groupId));
					  return;
		}
		
		if (res==RestResponseCode.OK)
		{
			inputOutput.put(String.format("Patient with id %i1 set to healthgroup with id %i2 ",
					 patientId, groupId));
					
		}
	}

}
