package tel_ran.hsa.model;

import java.time.*;
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

	@PersistenceContext(type=PersistenceContextType.EXTENDED)
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
		if ((em.find(DoctorOrm.class, doctor.getId()))!=null)
			return ALREADY_EXIST;
		DoctorOrm doctororm = new DoctorOrm(doctor.getId(), doctor.getName(), doctor.getPhoneNumber(), doctor.geteMail());
		em.persist(doctororm);
		return OK;
	}

	@Override
	@Transactional
	public String addPatient(Patient patient) {
//		HealthGroupOrm healthGroupOrm = em.find(HealthGroupOrm.class, patient.getHealthGroup().getGroupId());
//		if (healthGroupOrm==null) return NO_HEALTH_GROUP;
		if (em.find(PatientOrm.class, patient.getId())!=null) return ALREADY_EXIST;
//		DoctorOrm therapist = em.find(DoctorOrm.class, patient.getTherapist().getId());
//		PatientOrm patientOrm = new PatientOrm(patient.getId(), patient.getName(), patient.getPhoneNumber(), patient.geteMail(),healthGroupOrm,therapist);
		PatientOrm patientOrm = new PatientOrm(patient.getId(), patient.getName(), patient.getPhoneNumber(), patient.geteMail());
		em.persist(patientOrm);
		return OK;
	}

	@Override
	@Transactional
	public String removeDoctor(int doctorId) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctorId);
		if (doctororm==null)
			return NO_DOCTOR;
		em.refresh(doctororm);
		Set<VisitOrm> records = doctororm.getVisitsDoctors();
		records.removeIf(x->x.getDoctors().getId()==doctorId);
		em.remove(doctororm);
		return OK;
	}

	@Override
	@Transactional
	public String removePatient(int patientId) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm==null)
			return NO_PATIENT;
		em.refresh(patientorm);
		Set<VisitOrm> records = patientorm.getVisitsPatient();
		records.removeIf(x->x.getPatients().getId()==patientId);
		em.remove(patientorm);
		return OK;
	}

	@Override
	@Transactional
	public String updateDoctor(Doctor doctor) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctor.getId());
		if (doctororm==null)
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
		if (patientorm==null)
			return NO_PATIENT;
		patientorm.setId(patient.getId());
		patientorm.setName(patient.getName());
		patientorm.setPhoneNumber(patient.getPhoneNumber());
		patientorm.seteMail(patient.geteMail());
		HealthGroupOrm group = patientorm.getHealthGroup();
		if (group==null)
			return NO_HEALTH_GROUP;
		patientorm.setHealthGroup(group);
		em.persist(patientorm);
		return OK;
	}

	@Override
	public Doctor getDoctor(int docotrId) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, docotrId);
		if (doctororm==null)
			return null;
		Doctor doctor = new Doctor(docotrId, doctororm.getNameDoctor(), doctororm.getPhoneNumberByDoctor(), doctororm.geteMailDoctor());
		return doctor;
	}

	@Override
	public Patient getPatient(int patientId) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm==null)
			return null;
		/*HealthGroupOrm healthGroupOrm = patientorm.getHealthGroup();
		HealthGroup group = new HealthGroup(healthGroupOrm.getId(),healthGroupOrm.getName(),
				healthGroupOrm.getMinNormalPulse(),
				healthGroupOrm.getMaxNormalPulse(),
				healthGroupOrm.getServeyPeriod());*/
		Patient patient = new Patient(patientId, patientorm.getName(), patientorm.getPhoneNumber(), patientorm.geteMail());
		return patient;
	}

	@Override
	@Transactional
	public Iterable<Visit> buildSchedule(LocalDate startDate, LocalDate finishDate) throws ScheduleNotEmptyException {
		LocalDate endDate = finishDate.plusDays(1);
		List<VisitOrm> visits = em.createQuery("Select p from VisitOrm p", VisitOrm.class).getResultList();
		if (visits.stream().map(x->x.getDateTime().toLocalDate()).filter(date->date.isAfter(startDate.minusDays(1)))
				.filter(date->date.isBefore(endDate)).count()>0)
			throw new ScheduleNotEmptyException("There already is schedule on this period");
		
		ArrayList<Visit> list = new ArrayList<>();
		List<DoctorOrm> doctorsorm = em.createQuery("Select p from DoctorOrm p",DoctorOrm.class).getResultList();
		
		for (DoctorOrm doctororm:doctorsorm) {
			for (LocalDate currentDate=startDate; currentDate.isBefore(endDate);currentDate=currentDate.plusDays(1)) {
				for (TimeSlotOrm slot:doctororm.getSlots()) {
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
		while(currentDateTime.isBefore(endDateTime)) {
			VisitOrm visitorm = new VisitOrm(doctororm, null,currentDateTime, false);
			em.persist(visitorm);
			Doctor doc = new Doctor(doctororm.getId(), doctororm.getNameDoctor(), doctororm.getPhoneNumberByDoctor(), doctororm.geteMailDoctor());
			Visit visit = new Visit(doc, null, currentDateTime);
			result.add(visit);
			currentDateTime = currentDateTime.plusMinutes(timeSlot);
		}
		return result;
	}

	private TimeSlot getSlot(TimeSlotOrm slot) {
		TimeSlot res = new TimeSlot(slot.getNumberDayOfWeek(), slot.getBeginTime(), slot.getEndTime());
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
		em.refresh(doctororm);
		Set<VisitOrm> visits = doctororm.getVisitsDoctors();
		for (VisitOrm iter : visits) {
			if (iter == null)
				return NO_SCHEDULE;
			if (iter.getPatients() != null)
				return VISIT_BUSY;
			if (iter.getPatients() == null && iter.getDateTime() == dateTime) {
				iter.setPatients(patientorm);
				em.persist(iter);
				return OK;
			}

		}
		return "";
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
		em.refresh(doctororm);
		Set<VisitOrm> visits = doctororm.getVisitsDoctors();
		for (VisitOrm iter : visits) {
			if (iter == null)
				return NO_SCHEDULE;
			if (iter.getPatients() != patientorm)
				return VISIT_BUSY;
			if (iter.getPatients() == patientorm) {
				iter.setPatients(null);
				return OK;
			}
		}
		return "";
	}

	@Override
	public Iterable<Doctor> getPatientDoctors(int patientId) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm == null)
			return null;
		em.refresh(patientorm);
		Set<VisitOrm> visits = patientorm.getVisitsPatient();
		return visits.stream()
				.map(x -> new Doctor(x.getDoctors().getId(), x.getDoctors().getNameDoctor(),
						x.getDoctors().getPhoneNumberByDoctor(), x.getDoctors().geteMailDoctor()))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<Patient> getDoctorPatients(int docotrId) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, docotrId);
		if (doctororm == null)
			return null;
		em.refresh(doctororm);
		Set<VisitOrm> patients = doctororm.getVisitsDoctors();
		return patients.stream().map(x -> new Patient(x.getPatients().getId(), x.getPatients().getName(),
				x.getPatients().getPhoneNumber(), x.getPatients().geteMail())).collect(Collectors.toList());
				/*new HealthGroup(x.getPatients().getHealthGroup().getId()))).;
						x.getPatients().getHealthGroup().getName(),
						x.getPatients().getHealthGroup().getMinNormalPulse(),
						x.getPatients().getHealthGroup().getMaxNormalPulse(),
						x.getPatients().getHealthGroup().getServeyPeriod())))*/
				
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
		return getVisibleOrm(beginDate, endDate, records)
				.map(x -> new Visit(
						new Doctor(x.getDoctors().getId(), x.getDoctors().getNameDoctor(),
								x.getDoctors().getPhoneNumberByDoctor(), x.getDoctors().geteMailDoctor()),
						new Patient(x.getPatients().getId(), x.getPatients().getName(),
								x.getPatients().getPhoneNumber(), x.getPatients().geteMail()),
						x.getDateTime()))
				.collect(Collectors.toList());
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
		return getVisibleOrm(beginDate, endDate, records).filter(x -> x.getPatients() == null)
				.map(x -> new Visit(
						new Doctor(x.getDoctors().getId(), x.getDoctors().getNameDoctor(),
								x.getDoctors().getPhoneNumberByDoctor(), x.getDoctors().geteMailDoctor()),
						new Patient(x.getPatients().getId(), x.getPatients().getName(),
								x.getPatients().getPhoneNumber(), x.getPatients().geteMail()),
						x.getDateTime()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public String addPulseInfo(HeartBeat heartBeat) {
		PatientOrm patientorm = em.find(PatientOrm.class, heartBeat.getPatientId());
		if (patientorm == null)
			return NO_PATIENT;
		HeartBeatOrm pulse = new HeartBeatOrm(patientorm, heartBeat.getDateTime(), heartBeat.getValue(), patientorm.getHealthGroup().getServeyPeriod());
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
		List<LocalDate> listDate = pulsesPatient.stream()
				.map(date->date.getDateTime().toLocalDate()).collect(Collectors.toList());
		if (listDate.contains(beginDate) && listDate.contains(endDate)) {
			Set<HeartBeatOrm> pulsesbyDate = pulsesPatient.stream()
					.filter(x -> (x.getDateTime().toLocalDate().isAfter(beginDate)
							&& x.getDateTime().toLocalDate().isBefore(endDate)))
					.collect(Collectors.toSet());
			for (HeartBeatOrm iter : pulsesbyDate) {
				int S1=0,S2=0;
				for(HeartBeatOrm pair : pulsesbyDate) {
					S1 += pair.getValue();
					S2 +=pair.getSurveyPeriod();
				}
				if (pulsesbyDate.stream().map(period->period.getSurveyPeriod()).collect(Collectors.toList()).contains(surveyPeriod))
					return null;
				int k = iter.getValue();
				k = surveyPeriod*(S1/S2);
				res.add(k);
			}
		} else return null;

		return res;
	}

	@Override
	@Transactional
	public String replaceVisitsDoctor(int oldDoctorId, LocalDateTime beginDateTime,
			LocalDateTime endDateTime) {
		int count = 0;
		DoctorOrm doctororm = em.find(DoctorOrm.class, oldDoctorId);
		if (doctororm == null)
			return NO_DOCTOR;
		em.refresh(doctororm);
		Set<VisitOrm> visit = doctororm.getVisitsDoctors().stream()
				.filter(x->((x.getDateTime().isEqual(beginDateTime)
						&& x.getDateTime().isAfter(beginDateTime))
						&& (x.getDateTime().isEqual(endDateTime)
						&& x.getDateTime().isBefore(endDateTime)))).collect(Collectors.toSet());
		
		for (VisitOrm oldvisit:visit) {
			setPatient(oldvisit, beginDateTime,endDateTime,count);
			oldvisit.setPatients(null);
			oldvisit.setOccupied(true);
			em.persist(oldvisit);
			count++;
		}
		
		return OK;
	}

	private void setPatient(VisitOrm oldvisit,LocalDateTime beginDateTime,LocalDateTime endDateTime, int count) {
		List<VisitOrm> allVisits = em.createQuery("Select p from VisitOrm p", VisitOrm.class).getResultList().
				stream().filter(x->((x.getDateTime().isEqual(beginDateTime)
						&& x.getDateTime().isAfter(beginDateTime))
						&& (x.getDateTime().isEqual(endDateTime)
						&& x.getDateTime().isBefore(endDateTime)))).
				filter(visit->visit.getPatients()==null).collect(Collectors.toList());
		
		allVisits.get(count).setPatients(oldvisit.getPatients());
		em.persist(allVisits.get(count));
	}

	@Override
	@Transactional
	public String addHealthGroup(HealthGroup healthGroup) {
		if (em.find(HealthGroupOrm.class, healthGroup.getGroupId()) != null)
			return ALREADY_EXIST;
		HealthGroupOrm healthGroupOrm = new HealthGroupOrm(healthGroup.getGroupId(), healthGroup.getGroupName(),
				healthGroup.getMinNormalPulse(), healthGroup.getMaxNormalPulse(), healthGroup.getSurveyPeriod());
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
		patient.removeIf(x -> x.getHealthGroup().getId() == groupId); //***
		em.remove(healtgrouporm);
		return OK;
	}

	@Override
	public Iterable<HealthGroup> getHealthGroups() {
		List<HealthGroupOrm> healthGroupOrm = em.createQuery("Select p from HealthGroupOrm p", HealthGroupOrm.class)
				.getResultList();
		return healthGroupOrm.stream().map(x -> new HealthGroup(x.getId(), x.getName(), x.getMinNormalPulse(),
				x.getMaxNormalPulse(), x.getServeyPeriod())).collect(Collectors.toList());
	}

	@Override
	public Iterable<Patient> getPatients() {
		List<PatientOrm> patientOrm = em.createQuery("Select p from PatientOrm", PatientOrm.class).getResultList();
		return patientOrm.stream()
				.map(x -> new Patient(x.getId(), x.getName(), x.getPhoneNumber(), x.geteMail()))
				.collect(Collectors.toList());
	}

	private List<Doctor> getDoctor() {
		List<DoctorOrm> doctors = em.createQuery("Select p from DoctorOrm p",DoctorOrm.class).getResultList();
		return doctors.stream().map(d->new Doctor(d.getId(), d.getNameDoctor(), d.getPhoneNumberByDoctor(), d.geteMailDoctor())).collect(Collectors.toList());
	}
	@Override
	public Iterator<Doctor> iterator() {
		return getDoctor().iterator();
	}


	@Override
	public HealthGroup getHealthgroup(int groupId) {
		HealthGroupOrm healthGroupOrm = em.find(HealthGroupOrm.class, groupId);
		if (healthGroupOrm==null)
			return null;
		HealthGroup healthGroup = new HealthGroup(healthGroupOrm.getId(), healthGroupOrm.getName(),
				healthGroupOrm.getMinNormalPulse(),healthGroupOrm.getMaxNormalPulse(), healthGroupOrm.getServeyPeriod());
		return healthGroup;
	}

	@Override
	@Transactional
	public String setHealthGroup(int patientId, int groupId) {
		HealthGroupOrm healthGroupOrm = em.find(HealthGroupOrm.class, groupId);
		if (healthGroupOrm==null)
			return NO_HEALTH_GROUP;
		PatientOrm patient = em.find(PatientOrm.class, patientId);
		if (patient==null)
			return NO_PATIENT;
		patient.setHealthGroup(healthGroupOrm);
		em.persist(patient);
		return OK;
	}

	@Override
	public Iterable<Visit> getVisits(LocalDate beginDate, LocalDate endDate) {
		List<VisitOrm> visits = em.createQuery("Select p from VisitOrm p",VisitOrm.class).getResultList();
		List<VisitOrm> visitsByDate = visits.stream().
				filter(x->((x.getDateTime().toLocalDate().isEqual(beginDate) && x.getDateTime().toLocalDate().isAfter(beginDate))
						&& (x.getDateTime().toLocalDate().isEqual(endDate) && x.getDateTime().toLocalDate().isBefore(endDate)))).collect(Collectors.toList());
		
		return visitsByDate.stream().map(x -> new Visit(
				new Doctor(x.getDoctors().getId(), x.getDoctors().getNameDoctor(),
						x.getDoctors().getPhoneNumberByDoctor(), x.getDoctors().geteMailDoctor()),
				new Patient(x.getPatients().getId(), x.getPatients().getName(),
						x.getPatients().getPhoneNumber(), x.getPatients().geteMail()),
				x.getDateTime()))
		.collect(Collectors.toList());
	}

	@Override
	public Iterable<HeartBeat> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate) {
		PatientOrm patient = em.find(PatientOrm.class, patientId);
		if (patient==null)
			return null;
		em.refresh(patient);
		Set<HeartBeatOrm> beat = patient.getPulsePatients().stream().
				filter(x->((x.getDateTime().toLocalDate().isEqual(beginDate) && x.getDateTime().toLocalDate().isAfter(beginDate))
						&& (x.getDateTime().toLocalDate().isEqual(endDate) && x.getDateTime().toLocalDate().isBefore(endDate)))).collect(Collectors.toSet());
		
		return beat.stream().map(h->new HeartBeat(h.getPatient().getId(), h.getDateTime().toString(), h.getValue(),
				h.getSurveyPeriod())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public String setTimeSlot(int doctorId, TimeSlot... slots) {
		DoctorOrm doctor = em.find(DoctorOrm.class, doctorId);
		if (doctor==null)
			return NO_DOCTOR;
		doctor.setSlots(getSlots(slots));
		em.persist(doctor);
		return OK;
	}

	private Set<TimeSlotOrm> getSlots(TimeSlot[] slots) {
		Set<TimeSlot> res = new HashSet<>();
		for(TimeSlot iter:slots) {
			res.add(iter);
		}
		return res.stream().map(s->new TimeSlotOrm(s.getNumberDayOfWeek(),s.getBeginTime(), s.getEndTime())).collect(Collectors.toSet());
	}

	@Override
	public String setTherapist(int patientId, int doctorId) {
		PatientOrm patient = em.find(PatientOrm.class, patientId);
		if(patient==null)
			return NO_PATIENT;
		DoctorOrm doctor = em.find(DoctorOrm.class, doctorId);
		if (doctor==null)
			return NO_DOCTOR;
		patient.setTherapist(doctor);
		return OK;
	}

}
