package tel_ran.hsa.controller.items.data;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.protocols.api.RestResponseCode;

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
		if (res.equals(RestResponseCode.NO_PATIENT))
		{
			inputOutput.put(String.format("Patient with id %d doesn`t exist",
					  patientId));
					  return;
		}

		if (res.equals(RestResponseCode.NO_HEALTH_GROUP))
		{
			inputOutput.put(String.format("Healthgroup with id %d doesn`t exist",
					  groupId));
					  return;
		}
		
		if (res.equals(RestResponseCode.OK))
		{
			inputOutput.put(String.format("Patient with id %d set to healthgroup with id %d ",
					 patientId, groupId));
					
		}
	}

}
