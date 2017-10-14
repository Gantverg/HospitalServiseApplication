package tel_ran.hsa.controller.items.reports;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.entities.dto.Doctor;

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
