package tel_ran.hospital.controller;

import java.time.LocalDate;

import tel_ran.hsa.model.dto.*;
//import tel_ran.hospital.entities.not_use.Doctor;
//import tel_ran.hospital.entities.not_use.Patient;
//import tel_ran.hospital.entities.not_use.Visit;
import tel_ran.security.accounting.Account;


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
			inputOutput.put(String.format("Doctor with id %s doesn`t exist"));
			return;
		}
		Iterable<Patient> patients = hospital.getDoctorPatients(doctorId);
		if (patients==null)
		{
			inputOutput.put(String.format("Doctor with id %s have no patients",doctorId));
			return;
		}
		inputOutput.put(patients);

		
	}

}
