package tel_ran.hsa.controller;

import tel_ran.hsa.entities.dto.HealthGroup;
import tel_ran.hsa.protocols.api.RestResponseCode;
import tel_ran.view.Item;

public class GetHealthGroup extends HospitalItem {

	@Override
	public String displayedName() {
		// TODO Auto-generated method stub
		return "Get hospitalgroup by id";
	}

	@Override
	public void perform() {
		int groupId = inputOutput.getInteger("Enter healthgroup id");
		HealthGroup group = hospital.getHealthgroup(groupId);
		 if (group==null)
		  {
		  inputOutput.put(String.format("Healthgroup with id %d doesn`t exist",
		  groupId));
		  return;
		  }
       inputOutput.put(group);
	}

}
