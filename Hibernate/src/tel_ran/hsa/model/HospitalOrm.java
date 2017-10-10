package tel_ran.hsa.model;

import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
import java.util.stream.*;

import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.entities.orm.*;

import tel_ran.hsa.protocols.api.*;
import tel_ran.hsa.tests.model.ScheduleNotEmptyException;

@SuppressWarnings("serial")
public class HospitalOrm extends Hospital implements RestResponseCode {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager em;

	public HospitalOrm() {
		super();
	}

	public HospitalOrm(String hospitalStartTime, String hospitalFinishTime, long timeSlot) {
		super(hospitalStartTime, hospitalFinishTime, timeSlot);
	}

	@Override
	@Transactional
	public String addDoctor(Doctor doctor) {
		if ((em.find(DoctorOrm.class, doctor.getId())) != null)
			return ALREADY_EXIST;
		em.persist(getDoctorOrm(doctor));
		return OK;
	}

	@Override
	@Transactional
	public String addPatient(Patient patient) {
		if (em.find(PatientOrm.class, patient.getId()) != null)
			return ALREADY_EXIST;

		HealthGroupOrm healthGroupOrm = null;
		HealthGroup healthGroup = patient.getHealthGroup();
		if (healthGroup != null) {
			healthGroupOrm = em.find(HealthGroupOrm.class, healthGroup.getGroupId());
			if (healthGroupOrm == null)
				return NO_HEALTH_GROUP;
		}

		DoctorOrm therapist = null;
		Doctor doctor = patient.getTherapist();
		if (doctor != null) {
			therapist = em.find(DoctorOrm.class, doctor.getId());
			if (therapist == null)
				return NO_DOCTOR;
		}
		em.persist(getPatientOrm(patient, healthGroupOrm, therapist));
		return OK;
	}

	@Override
	@Transactional
	public String removeDoctor(int doctorId) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctorId);
		if (doctororm == null)
			return NO_DOCTOR;
		em.refresh(doctororm);
		Set<VisitOrm> records = doctororm.getVisitsDoctors();
		records.removeIf(x -> x.getDoctors().getId() == doctorId);
		em.remove(doctororm);
		return OK;
	}

	@Override
	@Transactional
	public String removePatient(int patientId) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm == null)
			return NO_PATIENT;
		em.refresh(patientorm);
		Set<VisitOrm> records = patientorm.getVisitsPatient();
		records.removeIf(x -> x.getPatients().getId() == patientId);
		em.remove(patientorm);
		return OK;
	}

	@Override
	@Transactional
	public String updateDoctor(Doctor doctor) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctor.getId());
		if (doctororm == null)
			return NO_DOCTOR;
		doctororm.setId(doctor.getId());
		doctororm.setNameDoctor(doctor.getName());
		doctororm.setPhoneNumberByDoctor(doctor.getPhoneNumber());
		doctororm.seteMailDoctor(doctor.geteMail());
		em.persist(doctororm);
		return OK;
	}

	@Override
	@Transactional
	public String updatePatient(Patient patient) {
		PatientOrm patientorm = em.find(PatientOrm.class, patient.getId());
		if (patientorm == null)
			return NO_PATIENT;
		patientorm.setId(patient.getId());
		patientorm.setName(patient.getName());
		patientorm.setPhoneNumber(patient.getPhoneNumber());
		patientorm.seteMail(patient.geteMail());
		patientorm.setHealthGroup(getHealthGroupOrm(patient.getHealthGroup()));
		em.persist(patientorm);
		return OK;
	}

	@Override
	public Doctor getDoctor(int docotrId) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, docotrId);
		if (doctororm == null)
			return null;
		return doctororm.getDoctor();
	}

	@Override
	public Patient getPatient(int patientId) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm == null)
			return null;
		else
			return patientorm.getPatient();
	}

	@Override
	@Transactional
	public Iterable<Visit> buildSchedule(LocalDate startDate, LocalDate finishDate) throws ScheduleNotEmptyException {
		LocalDate endDate = finishDate.plusDays(1);
		List<VisitOrm> visits = em.createQuery("Select p from VisitOrm p", VisitOrm.class).getResultList();
		if (visits.stream().map(x -> x.getDateTime().toLocalDate()).filter(date -> date.isAfter(startDate.minusDays(1)))
				.filter(date -> date.isBefore(endDate)).count() > 0)
			throw new ScheduleNotEmptyException("There already is schedule on this period");

		ArrayList<Visit> list = new ArrayList<>();
		List<DoctorOrm> doctorsorm = em.createQuery("Select p from DoctorOrm p", DoctorOrm.class).getResultList();
		for (DoctorOrm doctororm : doctorsorm) {
			em.refresh(doctororm);
			for (LocalDate currentDate = startDate; currentDate
					.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
				for (TimeSlotOrm slot : doctororm.getSlots()) {
					if (getSlot(slot).isDateInSlot(currentDate))
						list.addAll(fillSlots(currentDate, doctororm, slot));
				}

			}
		}
		return list;
	}

	private Collection<? extends Visit> fillSlots(LocalDate currentDate, DoctorOrm doctororm, TimeSlotOrm slot) {
		List<Visit> result = new ArrayList<>();
		LocalDateTime currentDateTime = LocalDateTime.of(currentDate, slot.getBeginTime());
		LocalDateTime endDateTime = LocalDateTime.of(currentDate, slot.getEndTime());
		while (currentDateTime.isBefore(endDateTime)) {
			VisitOrm visitorm = new VisitOrm(doctororm, null, currentDateTime, false);
			em.persist(visitorm);
			Doctor doc = doctororm.getDoctor();
			Visit visit = new Visit(doc, null, currentDateTime);
			result.add(visit);
			currentDateTime = currentDateTime.plusMinutes(timeSlot);
		}
		return result;
	}

	private TimeSlot getSlot(TimeSlotOrm slot) {
		TimeSlot res = slot.getTimeSlot();
		return res;
	}

	@Override
	@Transactional
	public String bookVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctorId);
		if (doctororm == null)
			return NO_DOCTOR;
		if (patientorm == null)
			return NO_PATIENT;
		VisitOrm visitorm = em.createQuery(String.format("Select p from VisitOrm p where dateTime='%s' and doctors.id=%d",
									 dateTime.toString(), doctorId), VisitOrm.class).getSingleResult();
		if(visitorm==null)
			return NO_SCHEDULE;
		if(visitorm.isOccupied())
			return VISIT_BUSY;
		if(visitorm.getPatients()!=null)
			return VISIT_BUSY;
		visitorm.setPatients(patientorm);
		em.persist(visitorm);
		return OK;
	}

	@Override
	@Transactional
	public String cancelVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctorId);
		if (doctororm == null)
			return NO_DOCTOR;
		if (patientorm == null)
			return NO_PATIENT;
		VisitOrm visitorm = em.createQuery(String.format("Select p from VisitOrm p where dateTime='%s' and doctors.id=%d",
									 dateTime.toString(), doctorId), VisitOrm.class).getSingleResult();
		if(visitorm==null)
			return NO_SCHEDULE;
		if(visitorm.getPatients()==null)
			return VISIT_FREE;
		if(visitorm.getPatients()!=patientorm)
			return VISIT_BUSY;
		visitorm.setPatients(null);
		em.persist(visitorm);
		return OK;
	}

	@Override
	public Iterable<Doctor> getPatientDoctors(int patientId) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm == null)
			return null;
		em.refresh(patientorm);
		Set<VisitOrm> visits = patientorm.getVisitsPatient();
		return visits.stream().map(visit -> visit.getDoctors().getDoctor()).collect(Collectors.toSet());
	}

	@Override
	public Iterable<Patient> getDoctorPatients(int docotrId) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, docotrId);
		if (doctororm == null)
			return null;
		em.refresh(doctororm);
		Set<VisitOrm> patients = doctororm.getVisitsDoctors();
		return patients.stream()
				.filter(visitOrm->!visitOrm.isOccupied())
				.map(VisitOrm::getPatients)
				.filter(patientOrm->patientOrm!=null)
				.map(PatientOrm::getPatient)
				.collect(Collectors.toSet());
				//.map(visit -> visit.getPatients().getPatient()).collect(Collectors.toSet());
	}

	@Override
	public Iterable<Visit> getVisitsByPatient(int patientId, LocalDate beginDate, LocalDate endDate) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm == null)
			return null;
		em.refresh(patientorm);
		Set<VisitOrm> records = patientorm.getVisitsPatient();
		return getVisit(beginDate, endDate, records);
	}

	private List<Visit> getVisit(LocalDate beginDate, LocalDate endDate, Set<VisitOrm> records) {
		return getVisibleOrm(beginDate, endDate, records).map(VisitOrm::getVisit).collect(Collectors.toList());
	}

	private Stream<VisitOrm> getVisibleOrm(LocalDate beginDate, LocalDate endDate, Set<VisitOrm> records) {
		return records.stream().filter(x -> (x.getDateTime().toLocalDate().isAfter(beginDate)
				&& x.getDateTime().toLocalDate().isBefore(endDate)));
	}

	@Override
	public Iterable<Visit> getVisitsByDoctor(int doctorId, LocalDate beginDate, LocalDate endDate) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctorId);
		if (doctororm == null)
			return null;
		em.refresh(doctororm);
		Set<VisitOrm> records = doctororm.getVisitsDoctors();
		return getVisit(beginDate, endDate, records);
	}

	@Override
	public Iterable<Visit> getFreeVisits(int doctorId, LocalDate beginDate, LocalDate endDate) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctorId);
		if (doctororm == null)
			return null;
		em.refresh(doctororm);
		Set<VisitOrm> records = doctororm.getVisitsDoctors();
		return getVisibleOrm(beginDate, endDate, records)
				.filter(x -> x.getPatients() == null)
				.filter(x -> !x.isOccupied())
				.map(VisitOrm::getVisit)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public String addPulseInfo(HeartBeat heartBeat) {
		PatientOrm patientorm = em.find(PatientOrm.class, heartBeat.getPatientId());
		if (patientorm == null)
			return NO_PATIENT;
		HeartBeatOrm pulse = getHeartBeatOrm(patientorm, heartBeat);
		em.persist(pulse);
		return OK;
	}

	@Override
	public Iterable<Integer> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate, int surveyPeriod) {
		Set<Integer> res = new HashSet<>();
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm == null)
			return null;
		em.refresh(patientorm);
		Set<HeartBeatOrm> pulsesPatient = patientorm.getPulsePatients();
		List<LocalDate> listDate = pulsesPatient.stream().map(date -> date.getDateTime().toLocalDate())
				.collect(Collectors.toList());
		if (listDate.contains(beginDate) && listDate.contains(endDate)) {
			Set<HeartBeatOrm> pulsesbyDate = pulsesPatient.stream()
					.filter(x -> (x.getDateTime().toLocalDate().isAfter(beginDate)
							&& x.getDateTime().toLocalDate().isBefore(endDate)))
					.collect(Collectors.toSet());
			for (HeartBeatOrm iter : pulsesbyDate) {
				int S1 = 0, S2 = 0;
				for (HeartBeatOrm pair : pulsesbyDate) {
					S1 += pair.getValue();
					S2 += pair.getSurveyPeriod();
				}
				if (pulsesbyDate.stream().map(period -> period.getSurveyPeriod()).collect(Collectors.toList())
						.contains(surveyPeriod))
					return null;
				int k = iter.getValue();
				k = surveyPeriod * (S1 / S2);
				res.add(k);
			}
		} else
			return null;

		return res;
	}

	@Override
	@Transactional
	public String replaceVisitsDoctor(int oldDoctorId, LocalDateTime beginDateTime, LocalDateTime endDateTime) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, oldDoctorId);
		if (doctororm == null)
			return NO_DOCTOR;
		em.refresh(doctororm);
		LocalDateTime startDateTime = beginDateTime.minusSeconds(1);
		LocalDateTime finishDateTime = endDateTime.plusSeconds(1);
		
		String stringQuery = String.format("Select p from VisitOrm p where dateTime > '%s' and dateTime < '%s' and patients is Null and occupied=FALSE and doctors.id != %d", 
				startDateTime,finishDateTime,oldDoctorId);
		Map<LocalDateTime, List<VisitOrm>> freeVisits = em.createQuery(stringQuery , VisitOrm.class).getResultList()
				.stream()
				.collect(Collectors.groupingBy(VisitOrm::getDateTime, Collectors.toList()));
				
/*		List<VisitOrm> freeVisitsList = em.createQuery(String.format("Select p from VisitOrm p where dateTime > '%s' and dateTime < '%s' and patients is Null and occupied=FALSE", 
				startDateTime,finishDateTime),
	VisitOrm.class).getResultList();
		Map<LocalDateTime, List<VisitOrm>> freeVisits = freeVisitsList.stream()
				.collect(Collectors.groupingBy(VisitOrm::getDateTime, Collectors.toList()));
*/		
		Set<VisitOrm> oldVisits = doctororm.getVisitsDoctors().stream()
				.filter(x -> x.getDateTime().isAfter(startDateTime)	&& x.getDateTime().isBefore(finishDateTime))
				.collect(Collectors.toSet());
		Set<VisitOrm> result = new HashSet<>();
		for(VisitOrm oldVisit: oldVisits) {
			if(oldVisit.getPatients()!=null) {
				VisitOrm newVisit = replaceVisit(oldVisit, freeVisits);
				if(newVisit == null) {
					return NO_SCHEDULE;
				}
				result.add(newVisit);
			} else {
				oldVisit.setOccupied(true);
			}
		}
		oldVisits.forEach(em::persist);
		result.forEach(em::persist);
		
		return OK;
	}

	private VisitOrm replaceVisit(VisitOrm oldVisit, Map<LocalDateTime, List<VisitOrm>> freeVisits) {
		List<VisitOrm> listVisits = freeVisits.get(oldVisit.getDateTime());
		if(listVisits == null)
			return null;
		VisitOrm result = listVisits.remove(0);
		if(listVisits.isEmpty()) {
			freeVisits.remove(oldVisit.getDateTime());
		}
		result.setPatients(oldVisit.getPatients());
		oldVisit.setPatients(null);
		oldVisit.setOccupied(true);
		em.persist(oldVisit);
		return result;
	}

	@Override
	@Transactional
	public String addHealthGroup(HealthGroup healthGroup) {
		if (em.find(HealthGroupOrm.class, healthGroup.getGroupId()) != null)
			return ALREADY_EXIST;
		HealthGroupOrm healthGroupOrm = getHealthGroupOrm(healthGroup);
		em.persist(healthGroupOrm);
		return OK;
	}

	@Override
	@Transactional
	public String removeHealthGroup(int groupId) {
		HealthGroupOrm healtgrouporm = em.find(HealthGroupOrm.class, groupId);
		if (healtgrouporm == null)
			return NO_HEALTH_GROUP;
		em.refresh(healtgrouporm);
		Set<PatientOrm> patient = healtgrouporm.getPatient();
		patient.removeIf(x -> x.getHealthGroup().getId() == groupId); // ***
		em.remove(healtgrouporm);
		return OK;
	}

	@Override
	public Iterable<HealthGroup> getHealthGroups() {
		List<HealthGroupOrm> healthGroupOrm = em.createQuery("Select p from HealthGroupOrm p", HealthGroupOrm.class)
				.getResultList();
		return healthGroupOrm.stream().map(HealthGroupOrm::getHealthGroup).collect(Collectors.toList());
	}

	@Override
	public Iterable<Patient> getPatients() {
		List<PatientOrm> patientOrm = em.createQuery("Select p from PatientOrm p", PatientOrm.class).getResultList();
		return patientOrm.stream().map(PatientOrm::getPatient).collect(Collectors.toList());
	}

	@Override
	public Iterable<Doctor> getDoctors() {
		List<DoctorOrm> doctors = em.createQuery("Select p from DoctorOrm p", DoctorOrm.class).getResultList();
		// em.refresh(doctors);
		return doctors.stream().map(DoctorOrm::getDoctor).collect(Collectors.toList());
	}

	@Override
	public Iterator<Doctor> iterator() {
		return getDoctors().iterator();
	}

	@Override
	public HealthGroup getHealthgroup(int groupId) {
		HealthGroupOrm healthGroupOrm = em.find(HealthGroupOrm.class, groupId);
		if (healthGroupOrm == null)
			return null;
		return healthGroupOrm.getHealthGroup();
	}

	@Override
	@Transactional
	public String setHealthGroup(int patientId, int groupId) {
		HealthGroupOrm healthGroupOrm = em.find(HealthGroupOrm.class, groupId);
		if (healthGroupOrm == null)
			return NO_HEALTH_GROUP;
		PatientOrm patient = em.find(PatientOrm.class, patientId);
		if (patient == null)
			return NO_PATIENT;
		patient.setHealthGroup(healthGroupOrm);
		em.persist(patient);
		return OK;
	}

	@Override
	public Iterable<Visit> getVisits(LocalDate beginDate, LocalDate endDate) {
		String startDate = beginDate.atStartOfDay().minusSeconds(1).toString();
		String finishDate = endDate.plusDays(1).atStartOfDay().toString();
		return em.createQuery(String.format("Select p from VisitOrm p where dateTime > '%s' and dateTime < '%s'", 
										startDate, 
										finishDate), 
					   VisitOrm.class)
			.getResultList()
			.stream()
			.map(VisitOrm::getVisit)
			.collect(Collectors.toList());
/*		
		
		
		List<VisitOrm> visits = em.createQuery("Select p from VisitOrm p", VisitOrm.class).getResultList();
		List<VisitOrm> visitsByDate = visits.stream().filter(x -> (x.getDateTime().toLocalDate().isAfter(startDate))
				&& (x.getDateTime().toLocalDate().isBefore(finishDate)))
				.collect(Collectors.toList());

		return visitsByDate.stream().map(VisitOrm::getVisit).collect(Collectors.toList());
*/	}

	@Override
	public Iterable<HeartBeat> getPulse(int patientId, LocalDate beginDate, LocalDate endDate) {
		PatientOrm patient = em.find(PatientOrm.class, patientId);
		if (patient == null)
			return null;
		em.refresh(patient);
		LocalDateTime startDate = beginDate.atStartOfDay().minusSeconds(1);
		LocalDateTime finishDate = endDate.plusDays(1).atStartOfDay();
		Set<HeartBeatOrm> beat = patient.getPulsePatients().stream()
				.filter(x -> (x.getDateTime().isAfter(startDate)
								&& x.getDateTime().isBefore(finishDate)))
				.collect(Collectors.toSet());

		return beat.stream().map(HeartBeatOrm::getHeartBeat).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public String setTimeSlot(int doctorId, TimeSlot... slots) {
		DoctorOrm doctor = em.find(DoctorOrm.class, doctorId);
		if (doctor == null)
			return NO_DOCTOR;
		getSlots(slots, doctor);
		//doctor.setSlots(getSlots(slots, doctor));
		//em.persist(doctor);
		return OK;
	}

	private Set<TimeSlotOrm> getSlots(TimeSlot[] slots, DoctorOrm doctor) {
		return Arrays.stream(slots)
				.map(s -> new TimeSlotOrm(s.getNumberDayOfWeek(), s.getBeginTime(), s.getEndTime(), doctor))
				.peek(em::persist)
				.collect(Collectors.toSet());
	}

	@Override
	@Transactional
	public String setTherapist(int patientId, int doctorId) {
		PatientOrm patient = em.find(PatientOrm.class, patientId);
		if (patient == null)
			return NO_PATIENT;
		DoctorOrm doctor = em.find(DoctorOrm.class, doctorId);
		if (doctor == null)
			return NO_DOCTOR;
		patient.setTherapist(doctor);
		em.persist(patient);
		return OK;
	}

	private HealthGroupOrm getHealthGroupOrm(HealthGroup group) {
		if (group == null)
			return null;
		return new HealthGroupOrm(group.getGroupId(), group.getGroupName(), group.getMinNormalPulse(),
				group.getMaxNormalPulse(), group.getSurveyPeriod());
	}

	private PatientOrm getPatientOrm(Patient patient, HealthGroupOrm healthGroupOrm, DoctorOrm therapistOrm) {
		PatientOrm patientOrm = new PatientOrm(patient.getId(), patient.getName(), patient.getPhoneNumber(),
				patient.geteMail(), healthGroupOrm, therapistOrm);
		//patientOrm.setHealthGroup(getHealthGroupOrm(patient.getHealthGroup()));
		//patientOrm.setTherapist(getDoctorOrm(patient.getTherapist()));
		return patientOrm;
	}

//	private TimeSlotOrm getTimeSlotOrm(TimeSlot timeSlot) {
//		return new TimeSlotOrm(timeSlot.getNumberDayOfWeek(), timeSlot.getBeginTime(), timeSlot.getEndTime());
//	}

	private DoctorOrm getDoctorOrm(Doctor doctor) {
		DoctorOrm doctorOrm = new DoctorOrm(doctor.getId(), doctor.getName(), doctor.getPhoneNumber(),
				doctor.geteMail());
		//TimeSlot[] timeSlots = doctor.getTimeSlots().toArray(new TimeSlot[doctor.getTimeSlots().size()]);
		//doctorOrm.setSlots(getSlots(timeSlots , doctorOrm));
		//doctorOrm.setSlots(doctor.getTimeSlots().stream().map(this::getTimeSlotOrm).collect(Collectors.toSet()));
		return doctorOrm;
	}

	private HeartBeatOrm getHeartBeatOrm(PatientOrm patientorm, HeartBeat heartBeat) {
		return new HeartBeatOrm(patientorm, heartBeat.getDateTime(), heartBeat.getValue(),
				patientorm.getHealthGroup().getSurveyPeriod());
	}
}
