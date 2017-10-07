package tel_ran.hsa.controller;

import tel_ran.hsa.entities.dto.Patient;

public class GetPatients extends HospitalItem {

	@Override
	public String displayedName() {
		// TODO Auto-generated method stub
		return "List of all patients:";
	}

	@Override
	public void perform() {
		Iterable<Patient> res = hospital.getPatients();
		if (res==null)
		{
			 inputOutput.put(String.format("No patients"));
			   return;
		}
		inputOutput.put(res);
	}

}
