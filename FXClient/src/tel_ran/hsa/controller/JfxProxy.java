package tel_ran.hsa.controller;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.entities.jfx.*;
import tel_ran.hsa.model.WebClient;
import tel_ran.hsa.model.interfaces.IHospital;
import tel_ran.hsa.tests.model.ScheduleNotEmptyException;

public class JfxProxy {

	WebClient hospital;
	
	public void setHospital(WebClient hospital) {
		this.hospital = hospital;
	}

	public JfxProxy(WebClient hospital) {
		super();
		this.hospital = hospital;
	}
	
	public String login(String user, String password) {
		hospital.setUserData(user, password);
		return hospital.login();
	}

	public String addDoctor(DoctorJfx doctor) {
		return hospital.addDoctor(doctor.get());
	}

	public String addPatient(PatientJfx patient) {
		return hospital.addPatient(patient.get());
	}

	public String addHealthGroup(HealthGroupJfx healthGroup) {
		return hospital.addHealthGroup(healthGroup.get());
	}

	public String removeDoctor(int doctorId) {
		return hospital.removeDoctor(doctorId);
	}

	public String removePatient(int patientId) {
		return hospital.removePatient(patientId);
	}

	public String removeHealthGroup(int groupId) {
		return hospital.removeHealthGroup(groupId);
	}

	public String updateDoctor(DoctorJfx doctor) {
		return hospital.updateDoctor(doctor.get());
	}

	public String updatePatient(PatientJfx patient) {
		return hospital.updatePatient(patient.get());
	}

	public DoctorJfx getDoctor(int doctorId) {
		Doctor doctor = hospital.getDoctor(doctorId);
		if(doctor != null)
			return new DoctorJfx(doctor);
		else
			return null;
	}

	public PatientJfx getPatient(int patientId) {
		Patient patient = hospital.getPatient(patientId);
		if(patient != null)
			return new PatientJfx(patient);
		else
			return null;
		
	}

	public HealthGroupJfx getHealthgroup(int groupId) {
		HealthGroup healthGroup = hospital.getHealthgroup(groupId);
		if(healthGroup != null)
			return new HealthGroupJfx(healthGroup);
		else
			return null;
	}

	public Iterable<HealthGroupJfx> getHealthGroups() {
		Iterable<HealthGroup> healthGroups = hospital.getHealthGroups();
		if(healthGroups != null)
			return StreamSupport.stream(healthGroups.spliterator(), false)
				.map(HealthGroupJfx::new)
				.collect(Collectors.toList());
		else
			return null;
	}

	public Iterable<DoctorJfx> getDoctors() {
		Iterable<Doctor> doctors = hospital.getDoctors();
		if(doctors != null)
			return StreamSupport.stream(doctors.spliterator(), false)
					.map(DoctorJfx::new)
					.collect(Collectors.toList());
		else
			return null;
	}

	public Iterable<PatientJfx> getPatients() {
		Iterable<Patient> patients = hospital.getPatients();
		if(patients != null)
			return StreamSupport.stream(patients.spliterator(), false)
					.map(PatientJfx::new)
					.collect(Collectors.toList());
		else
			return null;
	}

	public Iterable<VisitJfx> getVisits(LocalDate beginDate, LocalDate endDate) {
		Iterable<Visit> visits = hospital.getVisits(beginDate, endDate);
		if(visits != null)
			return StreamSupport.stream(visits.spliterator(), false)
					.map(VisitJfx::new)
					.collect(Collectors.toList());
		else
			return null;
	}

	public String setTimeSlot(int doctorId, TimeSlotJfx... slots) {
		TimeSlot[] timeSlots = Arrays.stream(slots)
								.map(TimeSlotJfx::get)
								.toArray(size -> new TimeSlot[size]);
		return hospital.setTimeSlot(doctorId, timeSlots);
	}

	public String setHealthGroup(int patientId, int groupId) {
		return hospital.setHealthGroup(patientId, groupId);
	}

	public String setTherapist(int patientId, int doctorId) {
		return hospital.setTherapist(patientId, doctorId);
	}

	public Iterable<VisitJfx> buildSchedule(LocalDate startDate, LocalDate finishDate) throws ScheduleNotEmptyException {
		Iterable<Visit> visits = hospital.buildSchedule(startDate, finishDate);
		if (visits != null)
			return StreamSupport.stream(visits.spliterator(), false)
				.map(VisitJfx::new)
				.collect(Collectors.toList());
		else
			return null;
	}

	public Iterable<VisitJfx> buildScheduleByDoctor(LocalDate startDate, LocalDate finishDate, int doctorId)
			throws ScheduleNotEmptyException {
		Iterable<Visit> visits = hospital.buildScheduleByDoctor(startDate, finishDate, doctorId);
		if(visits != null)
			return StreamSupport.stream(visits.spliterator(), false)
					.map(VisitJfx::new)
					.collect(Collectors.toList());
		else
			return null;
	}

	public String bookVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		return hospital.bookVisit(doctorId, patientId, dateTime);
	}

	public String cancelVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		return hospital.cancelVisit(doctorId, patientId, dateTime);
	}

	public String replaceVisitsDoctor(int doctorId, LocalDateTime beginDateTime, LocalDateTime endDateTime) {
		return hospital.replaceVisitsDoctor(doctorId, beginDateTime, endDateTime);
	}

	public Iterable<DoctorJfx> getPatientDoctors(int patientId) {
		Iterable<Doctor> doctors = hospital.getPatientDoctors(patientId);
		if(doctors != null)
			return StreamSupport.stream(doctors.spliterator(), false)
					.map(DoctorJfx::new)
					.collect(Collectors.toList());
		else
			return null;
	}

	public Iterable<PatientJfx> getDoctorPatients(int doctorId) {
		Iterable<Patient> patients = hospital.getDoctorPatients(doctorId);
		if(patients != null)
			return StreamSupport.stream(patients.spliterator(), false)
					.map(PatientJfx::new)
					.collect(Collectors.toList());
		else
			return null;
	}

	public Iterable<VisitJfx> getVisitsByPatient(int patientId, LocalDate beginDate, LocalDate endDate) {
		Iterable<Visit> visits = hospital.getVisitsByPatient(patientId, beginDate, endDate);
		if(visits != null)
			return StreamSupport.stream(visits.spliterator(), false)
					.map(VisitJfx::new)
					.collect(Collectors.toList());
		else
			return null;
	}

	public Iterable<VisitJfx> getVisitsByDoctor(int doctorId, LocalDate beginDate, LocalDate endDate) {
		Iterable<Visit> visits = hospital.getVisitsByDoctor(doctorId, beginDate, endDate);
		if(visits != null)
			return StreamSupport.stream(visits.spliterator(), false)
					.map(VisitJfx::new)
					.collect(Collectors.toList());
		else
			return null;
	}

	public Iterable<VisitJfx> getFreeVisits(int doctorId, LocalDate beginDate, LocalDate endDate) {
		Iterable<Visit> visits = hospital.getFreeVisits(doctorId, beginDate, endDate);
		if(visits != null)
			return StreamSupport.stream(visits.spliterator(), false)
					.map(VisitJfx::new)
					.collect(Collectors.toList());
		else
			return null;
	}

	public Iterable<HeartBeatJfx> getPulse(int patientId, LocalDate beginDate, LocalDate endDate) {
		Iterable<HeartBeat> pulses = hospital.getPulse(patientId, beginDate, endDate);
		if(pulses != null)
			return StreamSupport.stream(pulses.spliterator(), false)
					.map(HeartBeatJfx::new)
					.collect(Collectors.toList());
		else
			return null;
	}

	public Iterable<Integer> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate, int surveyPeriod) {
		return hospital.getPulseByPeriod(patientId, beginDate, endDate, surveyPeriod);
	}

}