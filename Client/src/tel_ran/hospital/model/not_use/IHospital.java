package tel_ran.hospital.model.not_use;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import tel_ran.hospital.entities.not_use.Doctor;
import tel_ran.hospital.entities.not_use.HealthGroup;
import tel_ran.hospital.entities.not_use.Patient;
import tel_ran.hospital.entities.not_use.Visit;
import tel_ran.hospital.protocols.not_use.RestResponseCode;


public interface IHospital extends Serializable{
	String addDoctor(Doctor doctor);
	String addPatient(Patient patient);
	String removeDoctor(int doctorId);
	String removePatient(int patientId);
	String updateDoctor(Doctor doctor);
	String updatePatient(Patient patient);
	Doctor getDoctor(int doctorId);
	Patient getPatient(int patientId);
	
	Iterable<Visit> buildSchedule(LocalDate startDate, LocalDate finishDate);
	String bookVisit(int doctorId, int patientId, LocalDateTime dateTime);
	String cancelVisit(int doctorId, int patientId, LocalDateTime dateTime);
	String replaceVisitsDoctor(int oldDoctorId, int newDoctorId, LocalDateTime beginDateTime, LocalDateTime endDateTime);
	
	Iterable<Doctor> getPatientDoctors(int patientId);
	Iterable<Patient> getDoctorPatients(int doctorId);
	
	Iterable<Visit> getVisitsByPatient(int patientId, LocalDate beginDate, LocalDate endDate);
	Iterable<Visit> getVisitsByDoctor(int doctorId, LocalDate beginDate, LocalDate endDate);
	Iterable<Visit> getFreeVisits(int doctorId, LocalDate beginDate, LocalDate endDate);
	
	String addHealthGroup(String groupName, int minNormalPulse, int maxNormalPulse, int surveyPeriod);
	String removeHealthGroup(String groupName);
	Iterable<HealthGroup> getHealthGroups();
	
	default String addPulseInfo(int patientId, LocalDateTime dateTime, int value) {
		return RestResponseCode.OK;
	}
	default Iterable<Integer> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate) {
		return getPulseByPeriod(patientId, beginDate, endDate, getPatient(patientId).getHealthGroup().getServeyPeriod());
	}
	Iterable<Integer> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate, int surveyPeriod);	}
