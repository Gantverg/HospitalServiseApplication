package tel_ran.hsa.controller;

import tel_ran.hsa.entities.dto.HealthGroup;
import tel_ran.hsa.protocols.api.RestResponseCode;

//import tel_ran.hospital.entities.Doctor;
//import tel_ran.hospital.protocols.RestResponseCode;

public class AddHealthGroup extends HospitalItem {
	@Override
	public String displayedName() {
		return "Add healthgroup";
	}

	@Override
	public void perform() {
		
		Integer groupId=inputOutput.getInteger("Enter healthgroup id");
		String groupName=inputOutput.getString("Enter healthgroup name");
		Integer minNormalPulse=inputOutput.getInteger("Enter min normal pulse");
		Integer maxNormalPulse=inputOutput.getInteger("Enter max normal pulse");
		Integer surveyPeriod=inputOutput.getInteger("Enter survey period");

		String res=hospital.addHealthGroup(new HealthGroup(groupId, groupName, minNormalPulse, maxNormalPulse, surveyPeriod));
		if (res.equals(RestResponseCode.ALREADY_EXIST))
		{
			inputOutput.put(String.format("Healthgroup  already exist"));
			return;
		}
		if (res.equals(RestResponseCode.OK))
		{
		inputOutput.put(String.format("Healthgroup with name %d was added",
		groupName));
		}
	}

}
