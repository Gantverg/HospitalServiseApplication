package tel_ran.hsa.controller;

import java.time.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.*;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.interfaces.IHospital;
import tel_ran.hsa.protocols.api.RestRequest;
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

	@RequestMapping(value = RestRequest.DOCTORS, method = RequestMethod.POST)
	public String addDoctor(@RequestBody Doctor doctor) {
		return hospital.addDoctor(doctor);
	}

	@RequestMapping(value = RestRequest.PATIENTS, method = RequestMethod.POST)
	public String addPatient(@RequestBody Patient patient) {
		return hospital.addPatient(patient);
	}

	@RequestMapping(value = RestRequest.HEALTHGROUPS, method = RequestMethod.POST)
	public String addHealthGroup(@RequestBody HealthGroup healthGroup) {
		return hospital.addHealthGroup(healthGroup);
	}
	
	@RequestMapping(value = RestRequest.WORKINGDAYS, method = RequestMethod.POST)
	public String addWorkingDays(@RequestBody WorkingDays workingDays) {
		return hospital.addWorkingDays(workingDays);
	}
	
	@RequestMapping(value = RestRequest.DOCTORS, method = RequestMethod.PUT)
	public String updateDoctor(@RequestBody Doctor doctor) {
		return hospital.updateDoctor(doctor);
	}

	@RequestMapping(value = RestRequest.PATIENTS, method = RequestMethod.PUT)
	public String updatePatient(@RequestBody Patient patient) {
		return hospital.updatePatient(patient);
	}

	@RequestMapping(value = RestRequest.DOCTORS+"/{"+RestRequest.DOCTOR_ID+"}", method = RequestMethod.DELETE)
	public String removeDoctor(@PathVariable int doctorId) {
		return hospital.removeDoctor(doctorId);
	}

	@RequestMapping(value = RestRequest.PATIENTS+"/{"+RestRequest.PATIENT_ID+"}", method = RequestMethod.DELETE)
	public String removePatient(@PathVariable int patientId) {
		return hospital.removePatient(patientId);
	}

	@RequestMapping(value = RestRequest.HEALTHGROUPS+"/{"+RestRequest.GROUP_ID+"}", method = RequestMethod.DELETE)
	public String removeHealthGroup(@PathVariable int groupId) {
		return hospital.removeHealthGroup(groupId);
	}
	
	@RequestMapping(value = RestRequest.WORKINGDAYS+"/{"+RestRequest.DAYS_ID+"}", method = RequestMethod.DELETE)
	public String removeWorkingDays(int daysId) {
		return hospital.removeWorkingDays(daysId);
	}	

	@RequestMapping(value = RestRequest.DOCTORS+"/{"+RestRequest.DOCTOR_ID+"}", method = RequestMethod.GET)
	public Doctor getDoctor(@PathVariable int doctorId) {
		return hospital.getDoctor(doctorId);
	}

	@RequestMapping(value = RestRequest.DOCTORS, method = RequestMethod.GET)
	public Iterable<Doctor> getDoctors() {
		return hospital.getDoctors(); 
	}

	@RequestMapping(value = RestRequest.PATIENTS+"/{"+RestRequest.PATIENT_ID+"}", method = RequestMethod.GET)
	public Patient getPatient(@PathVariable int patientId) {
		return hospital.getPatient(patientId);
	}

	@RequestMapping(value = RestRequest.PATIENTS, method = RequestMethod.GET)
	public Iterable<Patient> getPatients() {
		return hospital.getPatients();
	}

	@RequestMapping(value = RestRequest.HEALTHGROUPS+"/{"+RestRequest.GROUP_ID+"}", method = RequestMethod.GET)
	public HealthGroup getHealthGroup(@PathVariable int groupId) {
		return hospital.getHealthgroup(groupId);
	}

	@RequestMapping(value = RestRequest.HEALTHGROUPS, method = RequestMethod.GET)
	public Iterable<HealthGroup> getHealthGroups() {
		return hospital.getHealthGroups();
	}

	@RequestMapping(value = RestRequest.WORKINGDAYS+"/{"+RestRequest.DAYS_ID+"}", method = RequestMethod.GET)
	public WorkingDays getWorkingDays(@PathVariable int daysId) {
		return hospital.getWorkingDays(daysId);
	}	

	@RequestMapping(value = RestRequest.WORKINGDAYS, method = RequestMethod.GET)
	public Iterable<WorkingDays> getAllWorkingDays() {
		return hospital.getAllWorkingDays();
	}	

	@RequestMapping(value = RestRequest.VISITS, method = RequestMethod.PUT)
	public String bookVisit(@RequestParam(name=RestRequest.DATE_TIME) LocalDateTime dateTime,
						    @RequestParam(name=RestRequest.DOCTOR_ID) int doctorId,
						    @RequestParam(name=RestRequest.PATIENT_ID) int patientId) {
		return hospital.bookVisit(doctorId, patientId, dateTime);
	}

	@RequestMapping(value = RestRequest.VISITS, method = RequestMethod.DELETE)
	public String cancelVisit(@RequestParam(name=RestRequest.DATE_TIME) LocalDateTime dateTime,
						      @RequestParam(name=RestRequest.DOCTOR_ID) int doctorId,
						      @RequestParam(name=RestRequest.PATIENT_ID) int patientId) {
		return hospital.cancelVisit(doctorId, patientId, dateTime);
	}

	@RequestMapping(value = RestRequest.VISITS, method = RequestMethod.POST)
	public Iterable<Visit> buildSchedule(@RequestParam(name=RestRequest.BEGIN_DATE) LocalDate beginDate,
										 @RequestParam(name=RestRequest.END_DATE) LocalDate endDate) {
		try {
			return hospital.buildSchedule(beginDate, endDate);
		} catch (ScheduleNotEmptyException e) {
			return null;
		}
	}

	@RequestMapping(value = RestRequest.VISITS, method = RequestMethod.GET)
	public Iterable<Visit> getVisits(@RequestParam(name=RestRequest.BEGIN_DATE) LocalDate beginDate,
									 @RequestParam(name=RestRequest.END_DATE) LocalDate endDate) {
		return hospital.getVisits(beginDate, endDate);
	}
	
	@RequestMapping(value = RestRequest.VISITS + RestRequest.DOCTORS+"/{"+RestRequest.DOCTOR_ID+"}", method = RequestMethod.GET)
	public Iterable<Visit> getVisitsDoctors(@RequestParam(name=RestRequest.BEGIN_DATE) LocalDate beginDate,
			 					   			@RequestParam(name=RestRequest.END_DATE) LocalDate endDate,
			 					   			@PathVariable int doctorId) {
		return hospital.getVisitsByDoctor(doctorId, beginDate, endDate);
	}
	
	@RequestMapping(value = RestRequest.VISITS + RestRequest.PATIENTS+"/{"+RestRequest.PATIENT_ID+"}", method = RequestMethod.GET)
	public Iterable<Visit> getVisitsByPatient(@RequestParam(name=RestRequest.BEGIN_DATE) LocalDate beginDate,
	   										  @RequestParam(name=RestRequest.END_DATE) LocalDate endDate,
	   										  @PathVariable int patientId) {
		return hospital.getVisitsByPatient(patientId, beginDate, endDate);
	}
	
	@RequestMapping(value = RestRequest.VISITS + RestRequest.DOCTORS+"/{"+RestRequest.DOCTOR_ID+"}" + RestRequest.FREE, method = RequestMethod.GET)
	public Iterable<Visit> getVisitsDoctorsFree(@RequestParam(name=RestRequest.BEGIN_DATE) LocalDate beginDate,
			 					   				@RequestParam(name=RestRequest.END_DATE) LocalDate endDate,
			 					   				@PathVariable int doctorId) {
		return hospital.getFreeVisits(doctorId, beginDate, endDate);
	}
	
	@RequestMapping(value = RestRequest.DOCTORS+"/{"+RestRequest.DAYS_ID+"}" + 
							RestRequest.WORKINGDAYS+"/{"+RestRequest.DAYS_ID+"}", method = RequestMethod.PUT)
	public String getDoctorsWorkingDays(@PathVariable int doctorId,
								    	@PathVariable int daysId) {
		return hospital.setWorkingDays(doctorId, daysId);
	}	

	@RequestMapping(value = RestRequest.PATIENTS+"/{"+RestRequest.PATIENT_ID+"}" + 
							RestRequest.HEALTHGROUPS+"/{"+RestRequest.GROUP_ID+"}", method = RequestMethod.PUT)
	public String setPatientHealthgroup(@PathVariable int patientId,
			 							@PathVariable int groupId) {
		return hospital.setHealthGroup(patientId, groupId);
	}

	@RequestMapping(value = RestRequest.PATIENTS+"/{"+RestRequest.PATIENT_ID+"}" + RestRequest.DOCTORS, method = RequestMethod.GET)
	public Iterable<Doctor> getPatientDoctors(@PathVariable int patientId) {
		return hospital.getPatientDoctors(patientId);
	}

	@RequestMapping(value = RestRequest.DOCTORS+"/{"+RestRequest.DOCTOR_ID+"}" + RestRequest.PATIENTS, method = RequestMethod.GET)
	public Iterable<Patient> getDoctorPatients(@PathVariable int doctorId) {
		return hospital.getDoctorPatients(doctorId);
	}


	
	@RequestMapping(value = RestRequest.PULSE, method = RequestMethod.POST)
	public String addPulseInfo(@RequestParam(name=RestRequest.HEARTBEAT) HeartBeat heartBeat) {
		return hospital.addPulseInfo(heartBeat);
	}

	@RequestMapping(value = RestRequest.PULSE+"/{"+RestRequest.PATIENT_ID+"}", method = RequestMethod.GET)
	public Iterable<HeartBeat> getPulseByPeriod(@RequestParam(name=RestRequest.BEGIN_DATE) LocalDate beginDate,
											  @RequestParam(name=RestRequest.END_DATE) LocalDate endDate,
											  @PathVariable int patientId) {
		return hospital.getPulseByPeriod(patientId, beginDate, endDate);
	}

	@RequestMapping(value = RestRequest.PULSE+"/{"+RestRequest.PATIENT_ID+"}/{"+RestRequest.SURVEY_PERIOD+"}", method = RequestMethod.GET)
	public Iterable<Integer> getPulseByPeriod(@RequestParam(name=RestRequest.BEGIN_DATE) LocalDate beginDate,
											  @RequestParam(name=RestRequest.END_DATE) LocalDate endDate,
											  @PathVariable int patientId,
											  @PathVariable int surveyPeriod) {
		return hospital.getPulseByPeriod(patientId, beginDate, endDate, surveyPeriod);
	}
}