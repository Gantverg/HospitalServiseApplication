package tel_ran.hsa.controller;

import tel_ran.hsa.entities.dto.HealthGroup;

public class GetHealthGroups extends HospitalItem {

	@Override
	public String displayedName() {
		return "Get list of healthgroups";
	}

	@Override
	public void perform() {
		
		Iterable<HealthGroup> healthGroups = hospital.getHealthGroups();
		if (healthGroups==null)
		{
			inputOutput.put(String.format("Healthgroups aren`t exist"));
			return;
		}
		inputOutput.put(healthGroups);

		
	}

}
