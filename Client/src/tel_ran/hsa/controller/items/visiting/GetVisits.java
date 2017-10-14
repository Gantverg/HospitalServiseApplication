package tel_ran.hsa.controller.items.visiting;

import java.time.LocalDate;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.entities.dto.Visit;

public class GetVisits extends HospitalItem {

	@Override
	public String displayedName() {
		
		return "List of all visits by period";
	}

	@Override
	public void perform() {
	LocalDate beginDate =inputOutput.getDate("Enter begin date of visit"+format,format);
    LocalDate endDate =inputOutput.getDate("Enter end date of visit"+format,format);
		Iterable<Visit> res = hospital.getVisits(beginDate, endDate);
		if (res==null)
		{
			 inputOutput.put(String.format("No  visits in range of date %s - %s",beginDate,endDate));
			   return;
		}
		inputOutput.put(res);
	}

}
