package tel_ran.hsa.controller;

import java.time.*;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.*;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.interfaces.IHospital;
import tel_ran.hsa.protocols.api.RestRequest;
import tel_ran.hsa.protocols.api.RestResponseCode;
import tel_ran.hsa.tests.model.ScheduleNotEmptyException;
import tel_ran.security.interfaces.IAccounts;

@SpringBootApplication
@RestController
@ImportResource({"classpath:hospital.xml","classpath:security.xml"})
//@ImportResource({"classpath:hospital.xml"})
public class WebController {
	@Autowired
	IHospital hospital;
	
	@Autowired
	IAccounts accounts;

	public void setHospital(IHospital hospital) {
		this.hospital = hospital;
	}

	@RequestMapping(value = RestRequest.DOCTOR_ADD, method = RequestMethod.POST)
	public String addDoctor(@RequestBody Doctor doctor) {
		return hospital.addDoctor(doctor);
	}

	@RequestMapping(value = RestRequest.PATIENT_ADD, method = RequestMethod.POST)
	public String addPatient(@RequestBody Patient patient) {
		return hospital.addPatient(patient);
	}

	@RequestMapping(value = RestRequest.DOCTOR_REMOVE, method = RequestMethod.POST)
	public String removeDoctor(@RequestBody int doctorId) {
		return hospital.removeDoctor(doctorId);
	}

	@RequestMapping(value = RestRequest.PATIENT_REMOVE, method = RequestMethod.POST)
	public String removePatient(@RequestBody int patientId) {
		return hospital.removePatient(patientId);
	}

	@RequestMapping(value = RestRequest.DOCTOR_UPDATE, method = RequestMethod.POST)
	public String updateDoctor(@RequestBody Doctor doctor) {
		return hospital.updateDoctor(doctor);
	}

	@RequestMapping(value = RestRequest.PATIENT_UPDATE, method = RequestMethod.POST)
	public String updatePatient(@RequestBody Patient patient) {
		return hospital.updatePatient(patient);
	}

	@RequestMapping(value = RestRequest.DOCTOR_GET, method = RequestMethod.POST)
	public Doctor getDoctor(@RequestBody int docotrId) {
		Doctor doctor = hospital.getDoctor(docotrId);
		return doctor;
	}

	@RequestMapping(value = RestRequest.PATIENT_GET, method = RequestMethod.POST)
	public Patient getPatient(@RequestBody int patientId) {
		return hospital.getPatient(patientId);
	}

	@RequestMapping(value = RestRequest.PATIENT_HEALTHGROUP_SET, method = RequestMethod.POST)
	public String setPatientHealthgroup(@RequestBody Map<String, String> mapBody) {
		int groupId = Integer.parseInt(mapBody.get("groupId"));
		int patientId = Integer.parseInt(mapBody.get("patientId"));
		return hospital.setHealthGroup(patientId, groupId);
	}

	@RequestMapping(value = RestRequest.SCHEDULE_BUILD, method = RequestMethod.POST)
	public Iterable<Visit> buildSchedule(@RequestBody Map<String, String> mapBody) {
		LocalDate startDate = LocalDate.parse(mapBody.get("startDate"));
		LocalDate finishDate = LocalDate.parse(mapBody.get("finishDate"));
		try {
			return hospital.buildSchedule(startDate, finishDate);
		} catch (ScheduleNotEmptyException e) {
			return null;
		}
	}

	@RequestMapping(value = RestRequest.VISIT_BOOK, method = RequestMethod.POST)
	public String bookVisit(@RequestBody Map<String, String> mapBody) {
		int doctorId = Integer.parseInt(mapBody.get("doctorId"));
		int patientId = Integer.parseInt(mapBody.get("patientId"));
		LocalDateTime dateTime = LocalDateTime.parse(mapBody.get("dateTime"));
		return hospital.bookVisit(doctorId, patientId, dateTime);
	}

	@RequestMapping(value = RestRequest.VISIT_CANCEL, method = RequestMethod.POST)
	public String cancelVisit(@RequestBody Map<String, String> mapBody) {
		int doctorId = Integer.parseInt(mapBody.get("doctorId"));
		int patientId = Integer.parseInt(mapBody.get("patientId"));
		LocalDateTime dateTime = LocalDateTime.parse(mapBody.get("dateTime"));
		return hospital.cancelVisit(doctorId, patientId, dateTime);
	}

	@RequestMapping(value = RestRequest.VISITS_REPLACE_DOCTOR, method = RequestMethod.POST)
	public String cancelVisitByPatient(@RequestBody Map<String, String> mapBody) {
		int oldDoctorId = Integer.parseInt(mapBody.get("oldDoctorId"));
		int newDoctorId = Integer.parseInt(mapBody.get("newDoctorId"));
		LocalDateTime beginDateTime = LocalDateTime.parse(mapBody.get("beginDateTime"));
		LocalDateTime endDateTime= LocalDateTime.parse(mapBody.get("endDateTime"));
		return hospital.replaceVisitsDoctor(oldDoctorId, newDoctorId, beginDateTime, endDateTime);
	}

	@RequestMapping(value = RestRequest.PATIENTS_DOCTOR_GET, method = RequestMethod.POST)
	public Iterable<Doctor> getPatientDoctors(@RequestBody int patientId) {
		return hospital.getPatientDoctors(patientId);
	}

	@RequestMapping(value = RestRequest.DOCTORS_PATIENT_GET, method = RequestMethod.POST)
	public Iterable<Patient> getDoctorPatients(@RequestBody int docotrId) {
		return hospital.getDoctorPatients(docotrId);
	}

	@RequestMapping(value = RestRequest.VISITS_GET_PATIENT, method = RequestMethod.POST)
	public Iterable<Visit> getVisitsByPatient(@RequestBody Map<String, String> mapBody) {
		int patientId = Integer.parseInt(mapBody.get("patientId"));
		LocalDate beginDate = LocalDate.parse(mapBody.get("beginDate"));
		LocalDate endDate = LocalDate.parse(mapBody.get("endDate"));
		return hospital.getVisitsByPatient(patientId, beginDate, endDate);
	}

	@RequestMapping(value = RestRequest.VISITS_GET_DOCTOR, method = RequestMethod.POST)
	public Iterable<Visit> getVisitsByDoctor(@RequestBody Map<String, String> mapBody) {
		int doctorId = Integer.parseInt(mapBody.get("doctorId"));
		LocalDate beginDate = LocalDate.parse(mapBody.get("beginDate"));
		LocalDate endDate = LocalDate.parse(mapBody.get("endDate"));
		return hospital.getVisitsByDoctor(doctorId, beginDate, endDate);
	}

	@RequestMapping(value = RestRequest.VISITS_GET_FREE, method = RequestMethod.POST)
	public Iterable<Visit> getFreeVisits(@RequestBody Map<String, String> mapBody) {
		int doctorId = Integer.parseInt(mapBody.get("doctorId"));
		LocalDate beginDate = LocalDate.parse(mapBody.get("beginDate"));
		LocalDate endDate = LocalDate.parse(mapBody.get("endDate"));
		return hospital.getFreeVisits(doctorId, beginDate, endDate);
	}

	@RequestMapping(value = RestRequest.PULSE_ADD, method = RequestMethod.POST)
	public String addPulseInfo(@RequestBody Map<String, String> mapBody) {
		int patientId = Integer.parseInt(mapBody.get("patientId"));
		LocalDateTime dateTime = LocalDateTime.parse(mapBody.get("dateTime"));
		int value = Integer.parseInt(mapBody.get("value"));
		return hospital.addPulseInfo(patientId, dateTime, value);
	}

	@RequestMapping(value = RestRequest.PULSE_GET, method = RequestMethod.POST)
	public Iterable<Integer> getPulseByPeriod(@RequestBody Map<String, String> mapBody) {
		int patientId = Integer.parseInt(mapBody.get("patientId"));
		LocalDate beginDate = LocalDate.parse(mapBody.get("beginDate"));
		LocalDate endDate = LocalDate.parse(mapBody.get("endDate"));
		return hospital.getPulseByPeriod(patientId, beginDate, endDate);
	}

	@RequestMapping(value = RestRequest.HEALTHGROUP_ADD, method = RequestMethod.POST)
	public String addHealthGroup(@RequestBody Map<String, String> mapBody) {
		int groupId = Integer.parseInt(mapBody.get("groupId"));
		String groupName = mapBody.get("groupName");
		int minNormalPulse = Integer.parseInt(mapBody.get("minNormalPulse"));
		int maxNormalPulse = Integer.parseInt(mapBody.get("maxNormalPulse"));
		int serveyPeriod = Integer.parseInt(mapBody.get("surveyPeriod"));
		return hospital.addHealthGroup(new HealthGroup(groupId, groupName, minNormalPulse, maxNormalPulse, serveyPeriod));
	}
	
	@RequestMapping(value = RestRequest.HEALTHGROUP_REMOVE, method = RequestMethod.POST)
	public String removeHealthGroup(@RequestBody int groupId) {
		return hospital.removeHealthGroup(groupId);
	}
	
	@RequestMapping(value = RestRequest.HEALTHGROUPS_GET, method = RequestMethod.POST)
	public Iterable<HealthGroup> getHealthGroups() {
		return hospital.getHealthGroups();
	}

	@RequestMapping(value = RestRequest.WORKINGDAYS_ADD, method = RequestMethod.POST)
	public String addWorkingDays(@RequestBody Map<String, String> mapBody) {
		WorkingDays workingDays = new WorkingDays(Integer.parseInt(mapBody.get("daysId")));
		workingDays.setWorkDays(mapBody.entrySet().stream()
			.filter(entry->entry.getKey().contains("workDay"))
			.map(entry->DayOfWeek.valueOf(entry.getValue()))
			.collect(Collectors.toSet()));
		return hospital.addWorkingDays(workingDays);
	}
	
	@RequestMapping(value = RestRequest.WORKINGDAYS_REMOVE, method = RequestMethod.POST)
	public String removeWorkingDays(@RequestBody int daysId) {
		return hospital.removeWorkingDays(daysId);
	}	

	@RequestMapping(value = RestRequest.DOCTOR_WORKINGDAYS_SET, method = RequestMethod.POST)
	public String removeWorkingDays(@RequestBody Map<String, String> mapBody) {
		int doctorId = Integer.parseInt(mapBody.get("doctorId"));
		int daysId = Integer.parseInt(mapBody.get("daysId"));
		return hospital.setWorkingDays(doctorId, daysId);
	}	

	@RequestMapping(value = RestRequest.WORKINGDAYS_GET, method = RequestMethod.POST)
	public WorkingDays workingDays(@RequestBody int daysId) {
		return hospital.getWorkingDays(daysId);
	}	
}