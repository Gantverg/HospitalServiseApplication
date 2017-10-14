package tel_ran.hsa.controller.items.reports;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.entities.dto.*;


public class GetDoctorPatients extends HospitalItem {

	@Override
	public String displayedName() {
		return "Get list of doctor patients";
	}

	@Override
	public void perform() {
		Integer doctorId=inputOutput.getInteger("Enter doctor id");
		Doctor doctor = hospital.getDoctor(doctorId);
		if (doctor==null)
		{
			inputOutput.put(String.format("Doctor with id %d doesn`t exist",doctorId));
			return;
		}
		Iterable<Patient> patients = hospital.getDoctorPatients(doctorId);
		if (patients==null)
		{
			inputOutput.put(String.format("Doctor with id %d have no patients",doctorId));
			return;
		}
		inputOutput.put(patients);

		
	}

}
