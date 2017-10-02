package tel_ran.hsa.tests.model;

import java.time.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.Hospital;
import tel_ran.hsa.protocols.api.RestResponseCode;

@SuppressWarnings("serial")
public class HospitalProto extends Hospital {
	Map<Integer, Doctor> doctors;
	Map<Integer, Patient> patients;
	Map<Integer, HealthGroup> healthGroups;
	Map<PersonDateTime, Visit> schedule;
	Map<PersonDateTime, HeartBeat> pulseInfo;

	public HospitalProto(
			String hospitalStartTime, 
			String hospitalFinishTime, 
			long timeSlot) {
		super(hospitalStartTime, hospitalFinishTime, timeSlot);
		doctors = new HashMap<>();
		patients = new HashMap<>();
		healthGroups = new HashMap<>();
		schedule = new HashMap<>();
		pulseInfo = new HashMap<>();
		fillByDefault();

	}

	private void fillByDefault() {
		healthGroups = new HashMap<>();
		healthGroups.put(0, new HealthGroup(0, "Normal", 40, 80, 60*6));
		healthGroups.put(1, new HealthGroup(1, "Risk1", 80, 120, 30));
		healthGroups.put(2, new HealthGroup(2, "Risk2", 20, 60, 60));
		healthGroups.put(3, new HealthGroup(3, "Spies", 55, 65, 60));
	}
	
	private static TimeSlot buildSlot(DayOfWeek dayOfWeek, LocalTime beginTime, LocalTime endTime) {
		return new TimeSlot(dayOfWeek.getValue(), beginTime, endTime);
	}

	@Override
	public String addDoctor(Doctor doctor) {
		if(doctors.containsKey(doctor.getId()))
			return RestResponseCode.ALREADY_EXIST;
		doctors.put(doctor.getId(), doctor);
		return RestResponseCode.OK;
	}

	@Override
	public String addPatient(Patient patient) {
		return patients.putIfAbsent(patient.getId(), patient) == null ? RestResponseCode.OK
				: RestResponseCode.ALREADY_EXIST;
	}

	@Override
	public String removeDoctor(int doctorId) {
		return doctors.remove(doctorId) == null ? RestResponseCode.NO_DOCTOR : RestResponseCode.OK;
	}

	@Override
	public String removePatient(int patientId) {
		return patients.remove(patientId) == null ? RestResponseCode.NO_PATIENT : RestResponseCode.OK;
	}

	@Override
	public String updateDoctor(Doctor doctor) {
		if(doctors.containsKey(doctor.getId())) {
			doctors.put(doctor.getId(), doctor);
			return RestResponseCode.OK;
		} else {
			return RestResponseCode.NO_DOCTOR;
		}
	}

	@Override
	public String updatePatient(Patient patient) {
		if(patients.containsKey(patient.getId())) {
			patients.put(patient.getId(), patient);
			return RestResponseCode.OK;
		} else {
			return RestResponseCode.NO_PATIENT;
		}
	}

	@Override
	public Doctor getDoctor(int doctorId) {
		return doctors.get(doctorId);
	}

	@Override
	public Patient getPatient(int patientId) {
		return patients.get(patientId);
	}

	@Override
	public Iterable<Visit> buildSchedule(LocalDate startDate, LocalDate finishDate) throws ScheduleNotEmptyException {
		LocalDate endDate = finishDate.plusDays(1);
		if(schedule.keySet().stream()
			.map(k->k.dateTime.toLocalDate())
			.filter(date->date.isAfter(startDate.minusDays(1)))
			.filter(date->date.isBefore(endDate))
			.count() > 0)
			throw new ScheduleNotEmptyException("There already is schedule on this period");
		
		List<Visit> result = new ArrayList<>();
		for (Doctor doctor : doctors.values()) {
			for(LocalDate currentDate = startDate; currentDate.isBefore(endDate); 
					currentDate = currentDate.plusDays(1)) {
				for (TimeSlot slot : doctor.getTimeSlots()) {
					if(slot.isDateInSlot(currentDate))
						result.addAll(fillSlots(currentDate, doctor, slot));
				}
			}
		}
		return result;
	}

	private Collection<? extends Visit> fillSlots(LocalDate date, Doctor doctor, TimeSlot slot) {
		List<Visit> result = new ArrayList<>();
		LocalDateTime currentDateTime = LocalDateTime.of(date, slot.getBeginTime());
		LocalDateTime endDateTime = LocalDateTime.of(date, slot.getEndTime());
		while(currentDateTime.isBefore(endDateTime)) {
			Visit visit = new Visit(doctor, null, currentDateTime);
			result.add(visit);
			schedule.put(new PersonDateTime(doctor.getId(), currentDateTime), visit);
			currentDateTime = currentDateTime.plusMinutes(timeSlot);
		}
		return result;
	}

	@Override
	public String bookVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		if (!doctors.containsKey(doctorId))
			return RestResponseCode.NO_DOCTOR;
		Patient patient = patients.get(patientId);
		if (patient == null)
			return RestResponseCode.NO_PATIENT;
		PersonDateTime doctorDateTime = new PersonDateTime(doctorId, dateTime);
		if (!schedule.containsKey(doctorDateTime))
			return RestResponseCode.NO_SCHEDULE;
		Visit visit = schedule.get(doctorDateTime);
		if (visit.getPatient() != null)
			return RestResponseCode.VISIT_BUSY;
		visit.setPatient(patient);
		schedule.put(doctorDateTime, visit);
		return RestResponseCode.OK;
	}

	@Override
	public String cancelVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		if (!doctors.containsKey(doctorId))
			return RestResponseCode.NO_DOCTOR;
		Patient patient = patients.get(patientId);
		if (patient == null)
			return RestResponseCode.NO_PATIENT;
		PersonDateTime doctorDateTime = new PersonDateTime(doctorId, dateTime);
		if (!schedule.containsKey(doctorDateTime))
			return RestResponseCode.NO_SCHEDULE;
		Visit visit = schedule.get(doctorDateTime);
		if (visit.getPatient() == null)
			return RestResponseCode.VISIT_FREE;
		if (visit.getPatient() != patient)
			return RestResponseCode.VISIT_BUSY;
		visit.setPatient(null);
		return RestResponseCode.OK;
	}

	@Override
	public String replaceVisitsDoctor(int oldDoctorId, int newDoctorId, LocalDateTime beginDateTime,
			LocalDateTime endDateTime) {
		if (!doctors.containsKey(oldDoctorId))
			return RestResponseCode.NO_DOCTOR;
		if (!doctors.containsKey(newDoctorId))
			return RestResponseCode.NO_DOCTOR;
		Doctor newDoctor = doctors.get(newDoctorId);

		schedule.entrySet().stream().filter(entry -> entry.getKey().personId == oldDoctorId)
				.filter(entry -> entry.getKey().dateTime.isAfter(beginDateTime)
						&& entry.getKey().dateTime.isBefore(endDateTime))
				.forEach(entry -> {
					entry.getKey().personId = newDoctorId;
					entry.getValue().setDoctor(newDoctor);
				});
		return RestResponseCode.OK;
	}

	@Override
	public Iterable<Doctor> getPatientDoctors(int patientId) {
		return schedule.values().stream().filter(v -> v.getPatient().getId() == patientId).map(Visit::getDoctor)
				.collect(Collectors.toSet());
	}

	@Override
	public Iterable<Patient> getDoctorPatients(int doctorId) {
		return schedule.entrySet().stream().filter(entry -> entry.getKey().personId == doctorId)
				.map(entry -> entry.getValue().getPatient()).collect(Collectors.toSet());
	}

	@Override
	public Iterable<Visit> getVisitsByPatient(int patientId, LocalDate beginDate, LocalDate endDate) {
		return schedule.entrySet().stream().filter(entry -> entry.getValue().getPatient().getId() == patientId)
				.filter(entry -> isVisitInPeriod(beginDate, endDate, entry))
				.map(entry -> entry.getValue()).collect(Collectors.toList());
	}

	private boolean isVisitInPeriod(LocalDate beginDate, LocalDate endDate, Entry<PersonDateTime, Visit> entry) {
		LocalDate date = entry.getKey().dateTime.toLocalDate();
		return date.isAfter(beginDate.minusDays(1)) && date.isBefore(endDate.plusDays(1));
	}

	@Override
	public Iterable<Visit> getVisitsByDoctor(int doctorId, LocalDate beginDate, LocalDate endDate) {
		return schedule.entrySet().stream().filter(entry -> entry.getKey().personId == doctorId)
				.filter(entry -> isVisitInPeriod(beginDate, endDate, entry))
				.filter(entry -> entry.getValue().getPatient() != null)
				.map(entry -> entry.getValue())
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<Visit> getFreeVisits(int doctorId, LocalDate beginDate, LocalDate endDate) {
		return schedule.entrySet().stream().filter(entry -> entry.getKey().personId == doctorId)
				.filter(entry -> isVisitInPeriod(beginDate, endDate, entry))
				.filter(entry -> entry.getValue().getPatient() == null)
				.map(entry -> entry.getValue())
				.collect(Collectors.toList());
	}

	@Override
	public String addHealthGroup(HealthGroup healthGroup) {
		return healthGroups.putIfAbsent(healthGroup.getGroupId(), healthGroup) == null ? RestResponseCode.OK
						: RestResponseCode.ALREADY_EXIST;
	}

	@Override
	public String removeHealthGroup(int groupId) {
		return healthGroups.remove(groupId) == null ? RestResponseCode.NO_HEALTH_GROUP : RestResponseCode.OK;
	}

	@Override
	public Iterable<HealthGroup> getHealthGroups() {
		return healthGroups.values();
	}

	@Override
	public Iterable<Integer> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate, int surveyPeriod) {
		return pulseInfo.entrySet().stream().filter(entry -> entry.getKey().personId == patientId)
				.filter(entry -> entry.getValue().getDateTime().isAfter(beginDate.atStartOfDay())
						&& entry.getValue().getDateTime().isBefore(beginDate.atStartOfDay()))
				.map(entry -> entry.getValue().getValue()).collect(Collectors.toList());
	}

	@Override
	public String addPulseInfo(HeartBeat heartBeat) {
		Patient patient = patients.get(heartBeat.getPatientId());
		if (patient == null)
			return RestResponseCode.NO_PATIENT;

		return pulseInfo.putIfAbsent(new PersonDateTime(heartBeat.getPatientId(), heartBeat.getDateTime()),heartBeat) == null
						? RestResponseCode.OK
						: RestResponseCode.ALREADY_EXIST;
	}
	
	@Override
	public Iterable<Patient> getPatients() {
		return patients.values();
	}

	@Override
	public Iterator<Doctor> iterator() {
		return doctors.values().iterator();
	}

	@Override
	public HealthGroup getHealthgroup(int groupId) {
		return healthGroups.get(groupId);
	}

	@Override
	public String setHealthGroup(int patientId, int groupId) {
		Patient patient = patients.get(patientId);
		if(patient == null)
			return RestResponseCode.NO_PATIENT;
		HealthGroup healthGroup = healthGroups.get(groupId);
		if(healthGroup == null)
			return RestResponseCode.NO_HEALTH_GROUP;
		if(patient.getHealthGroup() == healthGroup)
			return RestResponseCode.ALREADY_EXIST;
		patient.setHealthGroup(healthGroup);
		return RestResponseCode.OK;
	}

	@Override
	public Iterable<Visit> getVisits(LocalDate beginDate, LocalDate endDate) {
		return schedule.values();
	}

	@Override
	public Iterable<HeartBeat> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate) {
		return pulseInfo.entrySet().stream()
				.filter(entry->entry.getKey().personId==patientId)
				.filter(entry->entry.getKey().dateTime.isAfter(beginDate.atStartOfDay()))
				.filter(entry->entry.getKey().dateTime.isBefore(endDate.plusDays(1).atStartOfDay()))
				.map(Entry::getValue)
				.collect(Collectors.toList());
	}

	@Override
	public String setTimeSlot(int doctorId, TimeSlot... slots) {
		Doctor doctor = doctors.get(doctorId);
		if(doctor==null)
			return RestResponseCode.NO_DOCTOR;
		doctor.setTimeSlots(new HashSet<>(Arrays.asList(slots)));
		return RestResponseCode.OK;
	}

}