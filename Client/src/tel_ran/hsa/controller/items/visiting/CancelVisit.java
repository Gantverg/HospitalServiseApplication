package tel_ran.hsa.controller.items.visiting;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.entities.dto.Visit;
import tel_ran.hsa.protocols.api.RestResponseCode;


public class CancelVisit extends HospitalItem {
	@Override
	public String displayedName() {
		return "Cancel visit to doctor";
	}

	@Override
	public void perform() {
		Integer doctorId=inputOutput.getInteger("Enter doctor id");
		Integer patientId=inputOutput.getInteger("Enter patient id");
		LocalDate beginDate=inputOutput.getDate
				("Enter pick begindate for cancelling visit in the format "+format, format);
		LocalDate endDate=inputOutput.getDate
				("Enter pick enddate for cancelling visit in the format "+format, format);
		Iterable<Visit> visits = hospital.getVisitsByPatient(patientId, beginDate, endDate);
		Iterator<Visit> iterator = visits.iterator();
		List<Visit> visitsList = new ArrayList<>();
		iterator.forEachRemaining(visitsList::add);
		if (visitsList.isEmpty())
		  {
		   inputOutput.put(String.format("No  visits by patient with id %d  in range of  date %s - %s",patientId,beginDate.toString(),endDate.toString()));
		   return;
		  }
		  List<Visit> visitsFilter = visitsList.stream().filter(x->x.getDoctor().getId()==doctorId).collect(Collectors.toList());
		  inputOutput.put(String.format("List of  visits by patient with id %d and doctor with id %d in range of dates %s - %s",patientId,doctorId,beginDate.toString(),endDate.toString()));
		  int i=1;
		  for (Visit iter:visitsFilter)
		  {
		   inputOutput.put(String.format("%d.Current visit time: %s",i++,iter.getDateTime().toString()));
		  }
		  int size = visitsFilter.size();
		  Integer listVisitsId=inputOutput.getInteger("Enter  visits digit in range of 1 - "+size);
		String res = hospital.cancelVisit(doctorId, patientId, visitsList.get(listVisitsId-1).getDateTime());
		if (res.equals(RestResponseCode.NO_DOCTOR))
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t exist", doctorId));
			return;
		}
		if (res.equals(RestResponseCode.NO_PATIENT))
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t exist", doctorId));
			return;
		}
		if (res.equals(RestResponseCode.NO_SCHEDULE))
		{
			inputOutput.put(String.format("Visit doesn`t exist in shedule", doctorId));
			return;
		}
		if (res.equals(RestResponseCode.OK)) {
		inputOutput.put(String.format("Doctor with id %d cancelled your visit at %s",
		doctorId,visitsFilter.get(listVisitsId-1).getDateTime().toString()));
		}
	}

}
