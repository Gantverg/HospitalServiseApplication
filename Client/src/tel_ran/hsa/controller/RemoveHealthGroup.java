package tel_ran.hsa.controller;

import tel_ran.hsa.protocols.api.RestResponseCode;

public class RemoveHealthGroup extends HospitalItem {
	@Override
	public String displayedName() {
		return "Remove a healthgroup";
	}

	@Override
	public void perform() {
		int groupId=inputOutput.getInteger("Enter id for removing");
		String res=hospital.removeHealthGroup(groupId);
		if (res==RestResponseCode.NO_HEALTH_GROUP)
		{
			inputOutput.put(String.format("Healthgroup with id %d doesn`t exist",groupId));
			return;
		}
		if (res==RestResponseCode.OK) {
		inputOutput.put(String.format("Healthgroup with id %d was removed",
		groupId));
		}
	}

}
