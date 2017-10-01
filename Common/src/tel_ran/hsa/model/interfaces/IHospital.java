package tel_ran.hsa.model.interfaces;

import java.io.Serializable;
import java.time.*;
import java.util.stream.*;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.protocols.api.RestResponseCode;
import tel_ran.hsa.tests.model.ScheduleNotEmptyException;

public interface IHospital extends Serializable, Iterable<Doctor>{
	String addDoctor(Doctor doctor);
	String addPatient(Patient patient);
	String addWorkingDays(WorkingDays workingDays);
	String addHealthGroup(HealthGroup healthGroup);

	String removeDoctor(int doctorId);
	String removePatient(int patientId);
	String removeHealthGroup(int groupId);
	String removeWorkingDays(int daysId);

	String updateDoctor(Doctor doctor);
	String updatePatient(Patient patient);

	Doctor getDoctor(int doctorId);
	Patient getPatient(int patientId);
	WorkingDays getWorkingDays(int daysId);
	HealthGroup getHealthgroup(int groupId);
	
	Iterable<HealthGroup> getHealthGroups();
	Iterable<WorkingDays> getAllWorkingDays();
	Iterable<Patient> getPatients();
	default Iterable<Doctor> getDoctors() {
		return StreamSupport.stream(this.spliterator(), false)
				.collect(Collectors.toList());
	}
	Iterable<Visit> getVisits(LocalDate beginDate, LocalDate endDate);

	String setWorkingDays(int doctorId, int daysId);
	String setHealthGroup(int patientId, int groupId);
	
	Iterable<Visit> buildSchedule(LocalDate startDate, LocalDate finishDate) throws ScheduleNotEmptyException;
	String bookVisit(int doctorId, int patientId, LocalDateTime dateTime);
	String cancelVisit(int doctorId, int patientId, LocalDateTime dateTime);
	String replaceVisitsDoctor(int oldDoctorId, int newDoctorId, LocalDateTime beginDateTime, LocalDateTime endDateTime);
	
	Iterable<Doctor> getPatientDoctors(int patientId);
	Iterable<Patient> getDoctorPatients(int doctorId);
	
	Iterable<Visit> getVisitsByPatient(int patientId, LocalDate beginDate, LocalDate endDate);
	Iterable<Visit> getVisitsByDoctor(int doctorId, LocalDate beginDate, LocalDate endDate);
	Iterable<Visit> getFreeVisits(int doctorId, LocalDate beginDate, LocalDate endDate);
	
	default String addPulseInfo(HeartBeat heartBeat) {
		return RestResponseCode.OK;
	}
	Iterable<HeartBeat> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate);
	Iterable<Integer> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate, int surveyPeriod);
	
}
