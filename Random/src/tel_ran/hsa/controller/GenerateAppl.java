package tel_ran.hsa.controller;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import tel_ran.hsa.tests.model.ScheduleNotEmptyException;
import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.interfaces.*;

//100 patients, 100 doctors
public class GenerateAppl {

	private static final int NUMBER_PATIENTS = 1000;
	private static final int MIN_ADD_PHONE = 11111111;
	private static final int MAX_ADD_PHONE = 99999999;
	private static final int NUMBER_DOCTORS = 10;
	private static final int NUMBER_HEALTH_GROUP = 4;
	private static final LocalDate BEGIN_DATE = LocalDate.now();
	private static final LocalDate END_DATE = BEGIN_DATE.plusDays(30);
	private static final int PERCENT_VISITS = 90;
	private static final int PERCENT_CANCEL = 10;
	static String phone, patientName, email, doctorName, groupName;
	static HealthGroup healthGroup;
	static Set<String> setNamesHealthgroups;
	static int minNormalPulse, maxNormalPulse, surveyPeriod;
	static HealthGroup healthgroup;
	static Doctor therapist;
	static List<HealthGroup> healthGroups;
	static List<Patient> patients;
	static List<Doctor> doctors;

	public static void main(String[] args) throws ScheduleNotEmptyException {

		System.out.println("Start at "+LocalDateTime.now());
		healthGroups = new ArrayList<>();
		patients = new ArrayList<>();
		doctors = new ArrayList<>();

		System.out.println("Generate healthgroups at "+LocalDateTime.now());
		healthGroups.add(new HealthGroup(0, "Normal", 40, 80, 3));
		healthGroups.add(new HealthGroup(1, "Risk1", 80, 120, 2));
		healthGroups.add(new HealthGroup(2, "Risk2", 20, 60, 2));
		healthGroups.add(new HealthGroup(3, "Spies", 55, 65, 1));
	
//		for (int i = 0; i < NUMBER_HEALTH_GROUP; i++) {
//			//generatorHealthGroups();
//			healthGroups.add(new HealthGroup(i+1, groupName, minNormalPulse, maxNormalPulse, surveyPeriod));
//		}

		System.out.println("Generate doctors at "+LocalDateTime.now());
		int id = NUMBER_PATIENTS*2;
		for (int i = 0; i < NUMBER_DOCTORS; i++) {
			generatorDoctors();
			doctors.add(new Doctor(id++, doctorName, phone, email));
		}

		System.out.println("Generate patients at "+LocalDateTime.now());
		id = 0;
		for (int i = 0; i < NUMBER_PATIENTS; i++) {
			generatorPatients();
			patients.add(new Patient(id++, patientName, phone, email, healthgroup, therapist));
		}


		TimeSlot[] timeslot1 = { new TimeSlot(1, LocalTime.of(8, 0), LocalTime.of(15, 0)),
				new TimeSlot(3, LocalTime.of(12, 0), LocalTime.of(19, 0)),
				new TimeSlot(5, LocalTime.of(11, 0), LocalTime.of(17, 0)),
				new TimeSlot(7, LocalTime.of(8, 0), LocalTime.of(15, 0)) };

		TimeSlot[] timeslot2 = { new TimeSlot(2, LocalTime.of(8, 0), LocalTime.of(15, 0)),
				new TimeSlot(4, LocalTime.of(12, 0), LocalTime.of(19, 0)),
				new TimeSlot(6, LocalTime.of(11, 0), LocalTime.of(17, 0)) };

		AbstractApplicationContext ctx = new FileSystemXmlApplicationContext("beans.xml");
		IHospital hospitalCreation = ctx.getBean(IHospital.class);

		healthGroups.stream().forEach(hospitalCreation::addHealthGroup);
		doctors.stream().forEach(hospitalCreation::addDoctor);
		patients.stream().forEach(hospitalCreation::addPatient);
		for (Doctor doctor : doctors) {
			int doctorId = doctor.getId();
			if (doctorId % 2 == 0)
				hospitalCreation.setTimeSlot(doctorId, timeslot1);
			else
				hospitalCreation.setTimeSlot(doctorId, timeslot2);
		}
		
		System.out.println("Build schedule at "+LocalDateTime.now());
		Iterable<Visit> visits =hospitalCreation.buildSchedule(BEGIN_DATE, END_DATE);

		//Iterable<Visit> visits = hospitalCreation.getVisits(BEGIN_DATE, END_DATE);
		List<Visit> visitsList = new ArrayList<>();
		visits.forEach(visitsList::add);
		// visitsList.forEach(System.out::println);

		System.out.println("Generate visits at "+LocalDateTime.now());
		int nVisits = visitsList.size()*PERCENT_VISITS/100;
		for (int i = 0; i < nVisits; i++) {
			Random rnd = new Random();
			Visit visitGen = visitsList.get(rnd.nextInt(visitsList.size() - 1));
			Patient patientGen = patients.get(rnd.nextInt(NUMBER_PATIENTS - 1));
			hospitalCreation.bookVisit(visitGen.getDoctor().getId(), patientGen.getId(), visitGen.getDateTime());
		}
		Iterable<Visit> visitsAfterBooking = hospitalCreation.getVisits(BEGIN_DATE, END_DATE);
		List<Visit> visitsListAfterBooking = new ArrayList<>();
		visitsAfterBooking.forEach(visitsListAfterBooking::add);
		List<Visit> visitsBookedList = visitsListAfterBooking.stream().filter(x -> x.getPatient() != null)
				.filter(x -> x.isBlocked() == false).collect(Collectors.toList());
		// System.out.println(visitsBookedList.size());
		System.out.println("Generate cancel visits at "+LocalDateTime.now());
		nVisits = visitsList.size()*PERCENT_CANCEL/100;
		for (int i = 0; i < nVisits; i++) {
			Random rnd = new Random();
			Visit visitForCancel = visitsBookedList.get(rnd.nextInt(visitsBookedList.size() - 1));
			hospitalCreation.cancelVisit(visitForCancel.getDoctor().getId(), visitForCancel.getPatient().getId(),
					visitForCancel.getDateTime());
		}

		System.out.println("Finished at "+LocalDateTime.now());
		/*
		 * Iterable<Visit> visitsAfterBooking1 = hospitalCreation.getVisits(BEGIN_DATE,
		 * END_DATE); List<Visit> visitsListAfterBooking1 = new ArrayList<>();
		 * visitsAfterBooking1.forEach(visitsListAfterBooking1::add); List<Visit>
		 * visitsBookedList1 =
		 * visitsListAfterBooking1.stream().filter(x->x.getPatient()!=null).filter(x->x.
		 * isBlocked()==false).collect(Collectors.toList());
		 * System.out.println(visitsBookedList1.size());
		 */

		/*
		 * patients.stream().forEach(System.out::println);
		 * System.out.println("________________________________");
		 * doctors.stream().forEach(System.out::println);
		 */
		ctx.close();

	}

	private static void generatorHealthGroups() {
		Random rnd = new Random();
		groupName = "group" + (rnd.nextInt(NUMBER_HEALTH_GROUP) + 1);
		minNormalPulse = 60 + rnd.nextInt(90);
		maxNormalPulse = 91 + rnd.nextInt(150);
		surveyPeriod = 15 + rnd.nextInt(150);

	}

	private static void generatorPatients() {
		Random rnd = new Random();
		patientName = "patient" + (rnd.nextInt(NUMBER_PATIENTS) + 1);
		phone = "05" + (MIN_ADD_PHONE + rnd.nextInt(MAX_ADD_PHONE));
		email = "ihospitalmail@gmail.com";//"email" + (rnd.nextInt(NUMBER_PATIENTS) + 1) + "@mail.com";
		healthgroup = healthGroups.get(rnd.nextInt(NUMBER_HEALTH_GROUP));
		therapist = doctors.get(rnd.nextInt(NUMBER_DOCTORS));
	}

	private static void generatorDoctors() {
		Random rnd = new Random();
		doctorName = "doctor" + (rnd.nextInt(NUMBER_DOCTORS) + 1);
		phone = "05" + (MIN_ADD_PHONE + rnd.nextInt(MAX_ADD_PHONE));
		email = "ihospitalmail@gmail.com";//"email" + (rnd.nextInt(NUMBER_DOCTORS + NUMBER_PATIENTS) + NUMBER_PATIENTS) + "@mail.com";
	}

}