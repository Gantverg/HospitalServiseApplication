package tel_ran.hospital.controller;

import java.time.LocalDate;

import tel_ran.hsa.model.dto.HealthGroup;
//import tel_ran.hospital.entities.not_use.Doctor;
//import tel_ran.hospital.entities.not_use.HealthGroup;
//import tel_ran.hospital.entities.not_use.Patient;
//import tel_ran.hospital.entities.not_use.Visit;
import tel_ran.security.accounting.Account;


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
