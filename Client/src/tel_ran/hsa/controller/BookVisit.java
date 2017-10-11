package tel_ran.hsa.controller;

import java.time.LocalDate;
import java.util.*;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.protocols.api.RestResponseCode;

public class BookVisit extends HospitalItem {
	@Override
	public String displayedName() {
		return "Book visit to doctor";
	}

	@Override
	public void perform() {
/*		Integer doctorId=inputOutput.getInteger("Enter doctor id");
		Integer patientId=inputOutput.getInteger("Enter patient id");
		Integer hours=inputOutput.getInteger("Enter hour for your visit");
		Integer minutes=inputOutput.getInteger("Enter minutes for your visit");
		LocalDateTime dateTime=inputOutput.getDate
				("Enter pick date in the format "+format, format).atTime(hours, minutes);


		
		String res = hospital.bookVisit(doctorId, patientId, dateTime);
		if (res==RestResponseCode.NO_DOCTOR)
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t exist", doctorId));
			return;
		}
		if (res==RestResponseCode.NO_PATIENT)
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t exist", doctorId));
			return;
		}
		if (res==RestResponseCode.NO_SCHEDULE)
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t have shedule for this time", doctorId));
			return;
		}
		if (res==RestResponseCode.VISIT_BUSY)
		{
			inputOutput.put(String.format("Doctor with id %d will have visit in this time", doctorId));
			return;
		}
		if (res==RestResponseCode.OK)
		{
		inputOutput.put(String.format("Doctor with id %d will wait your visit at %t",
		doctorId,dateTime.toString()));
		}
*/
		Integer doctorId=inputOutput.getInteger("Enter doctor id");
		  Doctor doctor = hospital.getDoctor(doctorId);
		  if (doctor==null)
		  {
		   inputOutput.put(String.format("Doctor with id %s doesn`t exist"));
		   return;
		  }
		  Integer patientId=inputOutput.getInteger("Enter patient id");
		  Patient patient = hospital.getPatient(patientId);
		  if (patient==null)
		  {
		   inputOutput.put(String.format("Patient with id %s doesn`t exist"));
		   return;
		  }
		  LocalDate beginDate =inputOutput.getDate("Enter begin date of visit"+format,format);
		  LocalDate endDate =inputOutput.getDate("Enter end date of visit"+format,format);

		  Iterable<Visit> freeVisits = hospital.getFreeVisits(doctorId, beginDate, endDate);
		  Iterator<Visit> iterator = freeVisits.iterator();
		  List<Visit> freeVisitsList = new ArrayList<>();
		  iterator.forEachRemaining(freeVisitsList::add);
//		  freeVisitsList.removeIf(x->x.getDoctor().getId()!=doctorId);
		  	
		  if (freeVisitsList.isEmpty())
		  {
		   inputOutput.put(String.format("No free visits by doctor with id %s in range of date %d1 - %d2",doctorId,beginDate,endDate));
		   return;
		  }
		  
		  inputOutput.put(String.format("List of free visits by doctor with id %s in range of date %s - %s",doctorId,beginDate.toString(),endDate.toString()));
		  int i=1;
		  for (Visit iter:freeVisitsList)
		  {
		   inputOutput.put(String.format("%d. Free visit time: %s",i++,iter.getDateTime().toString()));
		  }
		  int size = freeVisitsList.size();
		  Integer listVisitsId=inputOutput.getInteger("Enter free visit digit in range of 1 - "+size);
		  String res = hospital.bookVisit(doctorId, patientId, freeVisitsList.get(listVisitsId-1).getDateTime());
		  if (res.equals(RestResponseCode.OK))
		  {
		  inputOutput.put(String.format("Doctor with id %d will wait your visit at %t",
		  doctorId,freeVisitsList.get(listVisitsId-1).getDateTime().toString()));
		  }
		 }

}
