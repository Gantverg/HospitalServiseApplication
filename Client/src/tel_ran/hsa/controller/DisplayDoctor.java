package tel_ran.hsa.controller;

import tel_ran.hsa.entities.dto.Doctor;

//import tel_ran.hospital.entities.not_use.Doctor;

public class DisplayDoctor extends HospitalItem {
	@Override
	public String displayedName() {
		return "Display a doctor data";
	}

	@Override
	public void perform() {
		Integer doctorId=inputOutput.getInteger("Enter doctor id for search");
		Doctor doctor = hospital.getDoctor(doctorId);
		if (doctor==null) { 
			inputOutput.put(String.format("Doctor with id %d doesn`t exist", doctorId));
			return;
		}
		inputOutput.put(doctor);

	}

}
