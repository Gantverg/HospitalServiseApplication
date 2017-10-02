package tel_ran.hsa.tests;

import static org.junit.Assert.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import org.junit.Before;
import org.junit.Test;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.interfaces.IHospital;
import tel_ran.hsa.protocols.api.RestResponseCode;
import tel_ran.hsa.tests.model.HospitalProto;
import tel_ran.hsa.tests.model.ScheduleNotEmptyException;
import tel_ran.random.RandomGenerator;

public class ModelTestAppl {
	private static final int N_FIRST_DOCTOR = 0;
	private static final int N_DOCTORS = 100;
	private static final int PATIENTS_PER_DOCTOR = 10;
	private static final int N_FIRST_PATIENT = N_DOCTORS * PATIENTS_PER_DOCTOR;
	private static final int N_PATIENTS = N_DOCTORS * PATIENTS_PER_DOCTOR;
	private static List<HealthGroup> healthGroups = new ArrayList<>();
	private static final TimeSlot[][] timeSlots = {
					{buildSlot(DayOfWeek.SUNDAY,LocalTime.of(9, 0),LocalTime.of(18, 00)),
					 buildSlot(DayOfWeek.MONDAY,LocalTime.of(9, 0),LocalTime.of(18, 00)),
					 buildSlot(DayOfWeek.TUESDAY,LocalTime.of(9, 0),LocalTime.of(18, 00)),
					 buildSlot(DayOfWeek.WEDNESDAY,LocalTime.of(9, 0),LocalTime.of(18, 00)),
					 buildSlot(DayOfWeek.THURSDAY,LocalTime.of(9, 0),LocalTime.of(18, 00))},
					{buildSlot(DayOfWeek.SUNDAY,LocalTime.of(9, 0),LocalTime.of(12, 00)),
					 buildSlot(DayOfWeek.MONDAY,LocalTime.of(9, 0),LocalTime.of(12, 00)),
					 buildSlot(DayOfWeek.TUESDAY,LocalTime.of(9, 0),LocalTime.of(12, 00)),
					 buildSlot(DayOfWeek.WEDNESDAY,LocalTime.of(9, 0),LocalTime.of(12, 00)),
					 buildSlot(DayOfWeek.THURSDAY,LocalTime.of(9, 0),LocalTime.of(12, 00)),
					 buildSlot(DayOfWeek.FRIDAY,LocalTime.of(9, 0),LocalTime.of(12, 00))},
					{buildSlot(DayOfWeek.SUNDAY,LocalTime.of(9, 0),LocalTime.of(12, 00)),
					 buildSlot(DayOfWeek.SUNDAY,LocalTime.of(18, 0),LocalTime.of(23, 00)),
					 buildSlot(DayOfWeek.MONDAY,LocalTime.of(9, 0),LocalTime.of(12, 00)),
					 buildSlot(DayOfWeek.MONDAY,LocalTime.of(18, 0),LocalTime.of(23, 00)),
					 buildSlot(DayOfWeek.WEDNESDAY,LocalTime.of(9, 0),LocalTime.of(12, 00)),
					 buildSlot(DayOfWeek.WEDNESDAY,LocalTime.of(18, 0),LocalTime.of(23, 00)),
					 buildSlot(DayOfWeek.FRIDAY,LocalTime.of(9, 0),LocalTime.of(12, 00)),
					 buildSlot(DayOfWeek.FRIDAY,LocalTime.of(18, 0),LocalTime.of(23, 00))}
					};
	private static final LocalDate START_DATE = LocalDate.now();
	private static final LocalDate FINISH_DATE = START_DATE.plusDays(7);
	private static final LocalTime START_TIME = LocalTime.of(0, 0);
	private static final LocalTime FINISH_TIME = LocalTime.of(23, 59);
	private static final long TIME_SLOT = 15;
	IHospital hospital = new HospitalProto(START_TIME.toString(), FINISH_TIME.toString(), TIME_SLOT);
	RandomGenerator generator = new RandomGenerator();

	@Before
	public void setUp() throws Exception {
		Doctor doctor;
		for(int i = N_FIRST_DOCTOR; i < N_FIRST_DOCTOR + N_DOCTORS; i++) {
			doctor = new Doctor(i, 
							  String.format("doctor%02d", i), 
							  String.format("050-12345%02d", i), 
							  String.format("mail%02d@hospital.co.il", i));
			doctor.setTimeSlots(new HashSet<>(Arrays.asList(timeSlots[i%timeSlots.length])));
			hospital.addDoctor(doctor);
			
		};
		healthGroups.add(new HealthGroup(0, "Normal", 40, 80, 60*6));
		healthGroups.add(new HealthGroup(1, "Risk1", 80, 120, 30));
		healthGroups.add(new HealthGroup(2, "Risk2", 20, 60, 60));
		healthGroups.add(new HealthGroup(3, "Spies", 55, 65, 60));
		for (HealthGroup healthGroup : healthGroups) {
			hospital.addHealthGroup(healthGroup);
		}
		for(int i = N_FIRST_PATIENT; i < N_FIRST_PATIENT + N_PATIENTS; i++)
			hospital.addPatient(new Patient(i, 
										  String.format("patient%03d", i), 
										  String.format("051-1234%03d", i), 
										  String.format("sms%03d@hospital.co.il", i),
										  healthGroups.get(i%healthGroups.size())));
	}

	private static TimeSlot buildSlot(DayOfWeek dayOfWeek, LocalTime beginTime, LocalTime endTime) {
		return new TimeSlot(dayOfWeek.getValue(), beginTime, endTime);
	}

	@Test
	public void testAddDoctor() {
		assertEquals(N_DOCTORS, StreamSupport.stream(hospital.spliterator(), false).count());
		assertEquals(RestResponseCode.OK, hospital.addDoctor(
							    new Doctor(N_FIRST_DOCTOR + N_DOCTORS, 
								String.format("doctor%02d", N_FIRST_DOCTOR + N_DOCTORS), 
								String.format("050-12345%02d", N_FIRST_DOCTOR + N_DOCTORS), 
								String.format("mail%02d@hospital.co.il", N_FIRST_DOCTOR + N_DOCTORS))));
		assertEquals(N_DOCTORS + 1, StreamSupport.stream(hospital.spliterator(), false).count());
		assertEquals(RestResponseCode.ALREADY_EXIST, hospital.addDoctor(
							    new Doctor(N_FIRST_DOCTOR + N_DOCTORS, 
								String.format("doctor%02d", N_FIRST_DOCTOR + N_DOCTORS), 
								String.format("050-12345%02d", N_FIRST_DOCTOR + N_DOCTORS), 
								String.format("mail%02d@hospital.co.il", N_FIRST_DOCTOR + N_DOCTORS))));
		assertEquals(N_DOCTORS + 1, StreamSupport.stream(hospital.spliterator(), false).count());
	}
	
	@Test
	public void testAddPatient() {
		assertEquals(N_PATIENTS, StreamSupport.stream(hospital.getPatients().spliterator(),false).count());
		assertEquals(RestResponseCode.OK, hospital.addPatient(
							    new Patient(N_FIRST_PATIENT + N_PATIENTS, 
								String.format("patient%03d", N_FIRST_PATIENT + N_PATIENTS), 
								String.format("051-1234%03d", N_FIRST_PATIENT + N_PATIENTS), 
								String.format("sms%03d@hospital.co.il", N_FIRST_PATIENT + N_PATIENTS),
								healthGroups.get(0))));
		assertEquals(N_PATIENTS + 1, StreamSupport.stream(hospital.getPatients().spliterator(),false).count());
		assertEquals(RestResponseCode.ALREADY_EXIST, hospital.addPatient(
							    new Patient(N_FIRST_PATIENT + N_PATIENTS, 
								String.format("patient%03d", N_FIRST_PATIENT + N_PATIENTS), 
								String.format("051-1234%03d", N_FIRST_PATIENT + N_PATIENTS), 
								String.format("sms%03d@hospital.co.il", N_FIRST_PATIENT + N_PATIENTS),
								healthGroups.get(0))));
		assertEquals(N_PATIENTS + 1, StreamSupport.stream(hospital.getPatients().spliterator(),false).count());
	}
	
	@Test
	public void testAddHealthGroup() {
		assertEquals(RestResponseCode.OK, hospital.addHealthGroup(new HealthGroup(4, "additional", 60, 90, 24*60)));
		assertEquals(RestResponseCode.ALREADY_EXIST, hospital.addHealthGroup(new HealthGroup(4, "additional", 60, 90, 24*60)));
	}
	@Test
	public void testRemoveHealthGroup() {
		assertEquals(RestResponseCode.OK, hospital.addHealthGroup(new HealthGroup(4, "additional", 60, 90, 24*60)));
		assertEquals(RestResponseCode.OK, hospital.removeHealthGroup(4));
		assertEquals(RestResponseCode.NO_HEALTH_GROUP, hospital.removeHealthGroup(4));
	}
	
	@Test
	public void testRemoveDoctor() {
		assertEquals(RestResponseCode.OK, hospital.removeDoctor(N_FIRST_DOCTOR + N_DOCTORS - 1));
		assertEquals(N_DOCTORS - 1, StreamSupport.stream(hospital.spliterator(), false).count());
		assertEquals(RestResponseCode.OK, hospital.removeDoctor(N_FIRST_DOCTOR));
		assertEquals(N_DOCTORS - 2, StreamSupport.stream(hospital.spliterator(), false).count());
		assertEquals(RestResponseCode.NO_DOCTOR, hospital.removeDoctor(N_FIRST_DOCTOR));
		assertEquals(N_DOCTORS - 2, StreamSupport.stream(hospital.spliterator(), false).count());
		
	}

	@Test
	public void testRemovePatient() {
		assertEquals(RestResponseCode.OK, hospital.removePatient(N_FIRST_PATIENT + N_PATIENTS - 1));
		assertEquals(N_PATIENTS - 1, StreamSupport.stream(hospital.getPatients().spliterator(),false).count());
		assertEquals(RestResponseCode.OK, hospital.removePatient(N_FIRST_PATIENT));
		assertEquals(N_PATIENTS - 2, StreamSupport.stream(hospital.getPatients().spliterator(),false).count());
		assertEquals(RestResponseCode.NO_PATIENT, hospital.removePatient(N_FIRST_PATIENT));
		assertEquals(N_PATIENTS - 2, StreamSupport.stream(hospital.getPatients().spliterator(),false).count());
		
	}
	
	@Test
	public void testUpdateDoctor() {
		Doctor doctor = new Doctor(N_FIRST_DOCTOR, "Doctor123", "000-012345", "mail@mail.com");
		assertEquals(RestResponseCode.OK, hospital.updateDoctor(doctor));
		doctor = new Doctor(N_FIRST_DOCTOR + N_DOCTORS, "Doctor123", "000-012345", "mail@mail.com");
		assertEquals(RestResponseCode.NO_DOCTOR, hospital.updateDoctor(doctor));
		
	}

	@Test
	public void testUpdatePatient() {
		Patient patient = new Patient(N_FIRST_PATIENT, "Patient123", "000-012345", "mail@mail.com", healthGroups.get(0));
		assertEquals(RestResponseCode.OK, hospital.updatePatient(patient));
		patient = new Patient(N_FIRST_PATIENT + N_PATIENTS, "Patient123", "000-012345", "mail@mail.com", healthGroups.get(0));
		assertEquals(RestResponseCode.NO_PATIENT, hospital.updatePatient(patient));
		
	}
	
	@Test
	public void testGetDoctor() {
		for(int i = N_FIRST_DOCTOR; i < N_FIRST_DOCTOR + N_DOCTORS; i++)
			assertEquals(i, hospital.getDoctor(i).getId());
	}

	@Test
	public void testGetPatient() {
		for(int i = N_FIRST_PATIENT; i < N_FIRST_PATIENT + N_PATIENTS; i++)
			assertEquals(i, hospital.getPatient(i).getId());
	}
	
	@Test
	public void testGetDoctors() {
		List<Doctor> doctors = StreamSupport.stream(hospital.spliterator(), false).collect(Collectors.toList());
		assertEquals(N_DOCTORS, doctors.size());
		int i = 0;
		for (Doctor doctor : doctors) {
			assertEquals(N_FIRST_DOCTOR + i++, doctor.getId());
		}
	}
	
	@Test
	public void testGetPatients() {
		List<Patient> patients = StreamSupport.stream(hospital.getPatients().spliterator(), false).collect(Collectors.toList());
		assertEquals(N_PATIENTS, patients.size());
		int i = 0;
		for (Patient patient : patients) {
			assertEquals(N_FIRST_PATIENT + i++, patient.getId());
		}
	}
	
	private Iterable<Visit> generateSchedule() {
		try {
			return hospital.buildSchedule(START_DATE, FINISH_DATE);
		} catch (ScheduleNotEmptyException e) {
			fail("Wrong exception");
			return null;
		}
	}
	
	@Test
	public void testBuildSchedule() {
		Iterable<Visit> schedule = generateSchedule();

		LocalDateTime startDateTime = LocalDateTime.of(START_DATE, START_TIME).minusMinutes(1);
		LocalDateTime finishDateTime = LocalDateTime.of(FINISH_DATE, FINISH_TIME).plusMinutes(1);
		for (Visit visit : schedule) {
			LocalDateTime dateTime = visit.getDateTime();
			//all dates are inside period
			assertTrue(dateTime.isAfter(startDateTime));
			assertTrue(dateTime.isBefore(finishDateTime));
			
			LocalDate currentDate = visit.getDateTime().toLocalDate();
			//all dates are working dates
			assertTrue(visit.getDoctor().isDayWorking(currentDate));
		}
		//check: schedule is full
		List<Visit> listVisits = StreamSupport.stream(schedule.spliterator(), false).collect(Collectors.toList());
		for (int i = N_FIRST_DOCTOR; i < N_FIRST_DOCTOR + N_DOCTORS; i++) {
			Doctor doctor = hospital.getDoctor(i);
			for(LocalDate date = START_DATE; date.isBefore(FINISH_DATE.plusDays(1)); date = date.plusDays(1)) {
				
				LocalDateTime startDay = LocalDateTime.of(date, START_TIME);
				LocalDateTime finishDay = LocalDateTime.of(date, FINISH_TIME).plusMinutes(1);
				for(LocalDateTime dateTime = startDay; dateTime.isBefore(finishDay); dateTime = dateTime.plusMinutes(TIME_SLOT)) {
					Visit checkVisit = new Visit(doctor, null, dateTime);
					if(doctor.isDayTimeWorking(dateTime)) {
						assertTrue(listVisits.remove(checkVisit));
					} else {
						assertFalse(listVisits.remove(checkVisit));
					}
				}
			}
		}
		//check: there are not any visits
		assertTrue(listVisits.isEmpty());
		//try build one more time
		try {
			schedule = hospital.buildSchedule(START_DATE, FINISH_DATE);
			fail("Need exception");
		} catch (ScheduleNotEmptyException e) {
		}
	}
	
	@Test
	public void testBookVisit() {
		Iterable<Visit> schedule = generateSchedule();
		List<Visit> listVisits = StreamSupport.stream(schedule.spliterator(), false).collect(Collectors.toList());
		assertEquals(RestResponseCode.NO_DOCTOR,   hospital.bookVisit(N_FIRST_DOCTOR + N_DOCTORS, N_FIRST_PATIENT, LocalDateTime.of(START_DATE, START_TIME)));
		assertEquals(RestResponseCode.NO_PATIENT,  hospital.bookVisit(N_FIRST_DOCTOR, N_FIRST_PATIENT + N_PATIENTS, LocalDateTime.of(START_DATE, START_TIME)));
		assertEquals(RestResponseCode.NO_SCHEDULE, hospital.bookVisit(N_FIRST_DOCTOR, N_FIRST_PATIENT, LocalDateTime.of(START_DATE, START_TIME).minusMinutes(1)));
		assertEquals(RestResponseCode.NO_SCHEDULE, hospital.bookVisit(N_FIRST_DOCTOR, N_FIRST_PATIENT, LocalDateTime.of(START_DATE, START_TIME).minusDays(1)));
		assertEquals(RestResponseCode.NO_SCHEDULE, hospital.bookVisit(N_FIRST_DOCTOR, N_FIRST_PATIENT, LocalDateTime.of(FINISH_DATE, FINISH_TIME).plusMinutes(1)));
		assertEquals(RestResponseCode.NO_SCHEDULE, hospital.bookVisit(N_FIRST_DOCTOR, N_FIRST_PATIENT, LocalDateTime.of(FINISH_DATE, FINISH_TIME).plusDays(1)));
		Visit firstVisit = null;
		for (int doctorId = N_FIRST_DOCTOR; doctorId < N_FIRST_DOCTOR + N_DOCTORS; doctorId++) {
			Doctor doctor = hospital.getDoctor(doctorId);
			for(LocalDateTime dateTime = START_DATE.atStartOfDay(); 
				dateTime.isBefore(FINISH_DATE.plusDays(1).atStartOfDay()); 
				dateTime = dateTime.plusMinutes(TIME_SLOT)) {
				Visit checkVisit = new Visit(doctor, null, dateTime);
				int patientId = N_FIRST_PATIENT + doctorId * PATIENTS_PER_DOCTOR + generator.getRandomInteger(0, PATIENTS_PER_DOCTOR);
				if(doctor.isDayTimeWorking(dateTime)) {
					assertEquals(RestResponseCode.OK, hospital.bookVisit(doctorId, patientId, dateTime));
					assertTrue(listVisits.remove(checkVisit));
					if(firstVisit==null)
						firstVisit = checkVisit;
				} else {
					assertEquals(RestResponseCode.NO_SCHEDULE, hospital.bookVisit(doctorId, patientId, dateTime));
					assertFalse(listVisits.remove(checkVisit));
				}
			}
		}
		assertTrue(listVisits.isEmpty());
		assertEquals(RestResponseCode.VISIT_BUSY, hospital.bookVisit(firstVisit.getDoctor().getId(), 
																	 N_FIRST_PATIENT + N_PATIENTS - 1, 
																	 firstVisit.getDateTime()));
		
	}
	
	private List<Visit> generateVisits() {
		List<Visit> listVisits = new ArrayList<>();
		LocalDateTime startDateTime = LocalDateTime.of(START_DATE, START_TIME);
		LocalDateTime finishDateTime = LocalDateTime.of(FINISH_DATE, FINISH_TIME);
		for (int doctorId = N_FIRST_DOCTOR; doctorId < N_FIRST_DOCTOR + N_DOCTORS; doctorId++) {
			Doctor doctor = hospital.getDoctor(doctorId);
			for(LocalDateTime dateTime=startDateTime; dateTime.isBefore(finishDateTime); dateTime = dateTime.plusMinutes(TIME_SLOT)) {			
				if(doctor.isDayTimeWorking(dateTime)) {
					int patientId = N_FIRST_PATIENT + doctorId * PATIENTS_PER_DOCTOR + generator.getRandomInteger(0, PATIENTS_PER_DOCTOR);
					hospital.bookVisit(doctorId, patientId, dateTime);
					listVisits.add(new Visit(doctor, hospital.getPatient(patientId), dateTime));
				}
			}
		}
		return listVisits;
	}
	
	@Test
	public void testCancelVisit() {
		generateSchedule();
		List<Visit> listVisits = generateVisits();
		assertEquals(RestResponseCode.NO_DOCTOR, 	hospital.cancelVisit(N_FIRST_DOCTOR + N_DOCTORS, N_FIRST_PATIENT, LocalDateTime.of(START_DATE, START_TIME)));
		assertEquals(RestResponseCode.NO_PATIENT, 	hospital.cancelVisit(N_FIRST_DOCTOR, N_FIRST_PATIENT + N_PATIENTS, LocalDateTime.of(START_DATE, START_TIME)));
		assertEquals(RestResponseCode.NO_SCHEDULE, 	hospital.cancelVisit(N_FIRST_DOCTOR, N_FIRST_PATIENT, LocalDateTime.of(START_DATE, START_TIME).minusMinutes(1)));
		assertEquals(RestResponseCode.NO_SCHEDULE, 	hospital.cancelVisit(N_FIRST_DOCTOR, N_FIRST_PATIENT, LocalDateTime.of(START_DATE, START_TIME).minusDays(1)));
		assertEquals(RestResponseCode.NO_SCHEDULE, 	hospital.cancelVisit(N_FIRST_DOCTOR, N_FIRST_PATIENT, LocalDateTime.of(FINISH_DATE, FINISH_TIME).plusMinutes(1)));
		assertEquals(RestResponseCode.NO_SCHEDULE, 	hospital.cancelVisit(N_FIRST_DOCTOR, N_FIRST_PATIENT, LocalDateTime.of(FINISH_DATE, FINISH_TIME).plusDays(1)));
		assertEquals(RestResponseCode.VISIT_BUSY, 	hospital.cancelVisit(listVisits.get(0).getDoctor().getId()+1, 
																		 N_FIRST_PATIENT + N_PATIENTS-1, 
																		 listVisits.get(0).getDateTime()));
		assertEquals(RestResponseCode.OK, 			hospital.cancelVisit(listVisits.get(0).getDoctor().getId(), 
																		 listVisits.get(0).getPatient().getId(),
																		 listVisits.get(0).getDateTime()));
		assertEquals(RestResponseCode.VISIT_FREE, 	hospital.cancelVisit(listVisits.get(0).getDoctor().getId(), 
																		 listVisits.get(0).getPatient().getId(),
																		 listVisits.get(0).getDateTime()));
		
	}

	@Test
	public void testGetPatientDoctors() {
		generateSchedule();
		generateVisits();
		
		for (Patient patient : hospital.getPatients()) {
			int patientId = patient.getId();
			Iterable<Doctor> doctors = hospital.getPatientDoctors(patientId);
			assertTrue(doctors.iterator().hasNext());
			for (Doctor doctor : doctors) {
				assertEquals((patientId - N_FIRST_PATIENT)/PATIENTS_PER_DOCTOR, doctor.getId());
			}
		}
		
	}

	@Test
	public void testGetDoctorPatients() {
		generateSchedule();
		generateVisits();
		
		for (Doctor doctor : hospital) {
			int doctorId = doctor.getId();
			Iterable<Patient> patients = hospital.getDoctorPatients(doctorId);
			assertTrue(patients.iterator().hasNext());
			for (Patient patient : patients) {
				assertEquals(doctorId, (patient.getId() - N_FIRST_PATIENT)/ PATIENTS_PER_DOCTOR + N_FIRST_DOCTOR);
			}
		}
		
	}
	
	@Test
	public void testGetVisitsByPatient() {
		generateSchedule();
		List<Visit> listVisits = generateVisits();
		List<Visit> origPatientVisits = listVisits.stream()
			.filter(visit->visit.getPatient().getId()==N_FIRST_PATIENT)
			.collect(Collectors.toList());
		Iterable<Visit> patientVisits = hospital.getVisitsByPatient(N_FIRST_PATIENT, START_DATE, FINISH_DATE);
		List<Visit> compPatientVisits = StreamSupport.stream(patientVisits.spliterator(), false)
											.collect(Collectors.toList());
		for (Visit visit : origPatientVisits) {
			assertTrue(compPatientVisits.remove(visit));
		}
		assertTrue(compPatientVisits.isEmpty());
	}

	@Test
	public void testGetVisitsByDoctor() {
		generateSchedule();
		List<Visit> listVisits = generateVisits();
		List<Visit> origDoctorVisits = listVisits.stream()
			.filter(visit->visit.getDoctor().getId()==N_FIRST_DOCTOR)
			.collect(Collectors.toList());
		Iterable<Visit> doctorVisits = hospital.getVisitsByDoctor(N_FIRST_DOCTOR, START_DATE, FINISH_DATE);
		List<Visit> compDoctorVisits = StreamSupport.stream(doctorVisits.spliterator(), false)
											.collect(Collectors.toList());
		for (Visit visit : origDoctorVisits) {
			assertTrue(compDoctorVisits.remove(visit));
		}
		assertTrue(compDoctorVisits.isEmpty());
	}
	
	@Test
	public void testGetFreeVisits() {
		generateSchedule();
		List<Visit> listVisits = generateVisits();
		for (Visit visit : listVisits) {
			if(visit.getDoctor().getId()==N_FIRST_DOCTOR)
				hospital.cancelVisit(N_FIRST_DOCTOR, visit.getPatient().getId(), visit.getDateTime());
		}
		Iterable<Visit> freeVisits = hospital.getFreeVisits(N_FIRST_DOCTOR, START_DATE, START_DATE);
		for (Visit visit : freeVisits) {
			assertEquals(N_FIRST_DOCTOR, visit.getDoctor().getId());
			assertNull(visit.getPatient());
		}
		
		Iterable<Visit> busyVisits = hospital.getVisitsByDoctor(N_FIRST_DOCTOR, START_DATE, FINISH_DATE); //allready tested
		assertFalse(busyVisits.iterator().hasNext());
	}
}
