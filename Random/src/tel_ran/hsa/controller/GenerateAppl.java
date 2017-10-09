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

	private static final int NUMBER_PATIENTS = 100;
	private static final int MIN_ADD_PHONE = 11111111;
	private static final int MAX_ADD_PHONE = 99999999;
	private static final int NUMBER_DOCTORS = 10;
	private static final int NUMBER_HEALTH_GROUP = 3;
	private static final LocalDate BEGIN_DATE = LocalDate.now();
	private static final LocalDate END_DATE = BEGIN_DATE.plusDays(30);
	private static final int NUMBER_VISITS = 1000;
	private static final int NUMBER_VISITS_CANCEL = 50;
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

		for (int i = 0; i < NUMBER_HEALTH_GROUP; i++) {
			generatorHealthGroups();
			healthGroups.add(new HealthGroup(i+1, groupName, minNormalPulse, maxNormalPulse, surveyPeriod));
		}

		int id = 0;
		for (int i = 0; i < NUMBER_DOCTORS; i++) {
			generatorDoctors();
			doctors.add(new Doctor(id++, doctorName, phone, email));
		}

		id = Math.max(1000, NUMBER_DOCTORS);
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
		Iterable<Visit> visits =hospitalCreation.buildSchedule(BEGIN_DATE, END_DATE);

		//Iterable<Visit> visits = hospitalCreation.getVisits(BEGIN_DATE, END_DATE);
		List<Visit> visitsList = new ArrayList<>();
		visits.forEach(visitsList::add);
		// visitsList.forEach(System.out::println);
		for (int i = 0; i < NUMBER_VISITS; i++) {
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
		for (int i = 0; i < NUMBER_VISITS_CANCEL; i++) {
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
		// TODO Auto-generated method stub
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
		email = "email" + (rnd.nextInt(NUMBER_PATIENTS) + 1) + "@mail.com";
		healthgroup = healthGroups.get(rnd.nextInt(NUMBER_HEALTH_GROUP));
		therapist = doctors.get(rnd.nextInt(NUMBER_DOCTORS));
	}

	private static void generatorDoctors() {
		Random rnd = new Random();
		doctorName = "doctor" + (rnd.nextInt(NUMBER_DOCTORS) + 1);
		phone = "05" + (MIN_ADD_PHONE + rnd.nextInt(MAX_ADD_PHONE));
		email = "email" + (rnd.nextInt(NUMBER_DOCTORS + NUMBER_PATIENTS) + NUMBER_PATIENTS) + "@mail.com";
	}

}