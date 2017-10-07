package tel_ran.hsa.controller;

import tel_ran.hsa.entities.dto.Doctor;
import tel_ran.view.Item;

public class GetDoctors extends HospitalItem {

	@Override
	public String displayedName() {
		
		return "List of all doctors:";
	}

	@Override
	public void perform() {
		Iterable<Doctor> res = hospital.getDoctors();
		if (res==null)
		{
			 inputOutput.put(String.format("No  doctors"));
			   return;
		}
		inputOutput.put(res);
	}

}
