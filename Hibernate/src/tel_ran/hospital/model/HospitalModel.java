package tel_ran.hospital.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.transaction.annotation.Transactional;

import tel_ran.hospital.entitiesORM.*;
import tel_ran.hospital.entities.*;

import tel_ran.hospital.api.RestResponseCode;


public class HospitalModel implements IHospital, RestResponseCode {
	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	protected EntityManager em;
	
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
		HealthGroupOrm healthGroupOrm = em.find(HealthGroupOrm.class, patient.getHealthGroup().getId());
		if (healthGroupOrm==null) return NO_HEALTH_GROUP;
		if (em.find(PatientOrm.class, patient.getId())!=null) return ALREADY_EXIST;
		PatientOrm patientOrm = new PatientOrm(patient.getId(), patient.getName(), patient.getPhoneNumber(), patient.geteMail(),healthGroupOrm);
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
		em.refresh(doctororm);
		Set<VisitOrm> records = doctororm.getVisitsDoctors();
		for (VisitOrm iter:records) {
			iter.setDoctors(doctororm);
		}
		return OK;
	}

	@Override
	@Transactional
	public String updatePatient(Patient patient) {
		PatientOrm patientorm = em.find(PatientOrm.class, patient.getId());
		if (patientorm==null)
			return NO_PATIENT;
		em.refresh(patientorm);
		Set<VisitOrm> records = patientorm.getVisitsPatient();
		for (VisitOrm iter:records) {
			iter.setPatients(patientorm);
		}
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
		HealthGroup group = new HealthGroup(patientorm.getHealthGroup().getId(),patientorm.getHealthGroup().getName(),
				patientorm.getHealthGroup().getMinNormalPulse(),
				patientorm.getHealthGroup().getMaxNormalPulse(),
				patientorm.getHealthGroup().getServeyPeriod());
		Patient patient = new Patient(patientId, patientorm.getName(), patientorm.getPhoneNumber(), patientorm.geteMail(), group);
		return patient;
	}

	@Override
	@Transactional
	public Iterable<Visit> buildSchedule(LocalDate startDate, LocalDate finishDate) {
		ArrayList<Visit> list = new ArrayList<>();
		LocalTime timeStart = LocalTime.of(9, 00);
		LocalTime timeFinish = LocalTime.of(17, 30);
		List<DoctorOrm> doctorsorm = em.createQuery("Select p from DoctorOrm p",DoctorOrm.class).getResultList();
		for (DoctorOrm doctororm:doctorsorm) {
			for (LocalDate currentDate=startDate; currentDate.isBefore(finishDate) || currentDate.isEqual(finishDate);currentDate=currentDate.plusDays(1)) {
				for (LocalTime currentTime = timeStart; currentTime.isBefore(timeFinish); currentTime.plusMinutes(30)) {
					LocalDateTime currentDateTime = LocalDateTime.of(currentDate, currentTime);
					VisitOrm visitorm = new VisitOrm(doctororm, null,currentDateTime);
					em.persist(visitorm);
					Doctor doc = new Doctor(doctororm.getId(), doctororm.getNameDoctor(), doctororm.getPhoneNumberByDoctor(), doctororm.geteMailDoctor());
					Visit visit = new Visit(doc, null, visitorm.getDateTime());
					list.add(visit);
				}
				
			}
		}
		return list;
	}

	@Override
	@Transactional
	public String bookVisit(int doctorId, int patientId, LocalDateTime dateTime) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctorId);
		if (doctororm==null) return NO_DOCTOR;
		if (patientorm==null) return NO_PATIENT;
		em.refresh(doctororm);
		Set<VisitOrm> visits = doctororm.getVisitsDoctors();
		for(VisitOrm iter:visits) {
			if (iter==null)  return NO_SCHEDULE;
			if(iter.getPatients()!=null) return VISIT_BUSY;
			if (iter.getPatients()==null && iter.getDateTime()==dateTime) {
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
		if (doctororm==null) return NO_DOCTOR;
		if (patientorm==null) return NO_PATIENT;
		em.refresh(doctororm);
		Set<VisitOrm> visits = doctororm.getVisitsDoctors();
		for(VisitOrm iter:visits) {
			if (iter==null)  return NO_SCHEDULE;
			if (iter.getPatients()!=patientorm) return VISIT_BUSY;
			if (iter.getPatients()==patientorm) {iter.setPatients(null); return OK;}
		}
		return "";
	}

	@Override
	public Iterable<Doctor> getPatientDoctors(int patientId) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm==null)
		return null;
		em.refresh(patientorm);
		Set<VisitOrm> visits = patientorm.getVisitsPatient();
		return visits.stream().map(x->new Doctor(x.getDoctors().getId(),x.getDoctors().getNameDoctor(),
				x.getDoctors().getPhoneNumberByDoctor(),x.getDoctors().geteMailDoctor()))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<Patient> getDoctorPatients(int docotrId) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, docotrId);
		if (doctororm==null)
			return null;
		em.refresh(doctororm);
		Set<VisitOrm> patients = doctororm.getVisitsDoctors();
		return patients.stream().map(x->new Patient(x.getPatients().getId(), x.getPatients().getName(), x.getPatients().getPhoneNumber(),
				x.getPatients().geteMail(), 
				new HealthGroup(x.getPatients().getHealthGroup().getId(), x.getPatients().getHealthGroup().getName(),
						x.getPatients().getHealthGroup().getMinNormalPulse(),
						x.getPatients().getHealthGroup().getMaxNormalPulse(),
						x.getPatients().getHealthGroup().getServeyPeriod()))).collect(Collectors.toList());
	}

	@Override
	public Iterable<Visit> getVisitsByPatient(int patientId, LocalDate beginDate, LocalDate endDate) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm==null)
		return null;
		em.refresh(patientorm);
		Set<VisitOrm> records = patientorm.getVisitsPatient();
		return getVisit(beginDate, endDate, records);
	}

	private List<Visit> getVisit(LocalDate beginDate, LocalDate endDate, Set<VisitOrm> records) {
		return getVisibleOrm(beginDate, endDate, records)
				.map(x->new Visit(new Doctor(x.getDoctors().getId(), x.getDoctors().getNameDoctor(),
						x.getDoctors().getPhoneNumberByDoctor(), x.getDoctors().geteMailDoctor()),
						new Patient(x.getPatients().getId(), x.getPatients().getName(), x.getPatients().getPhoneNumber(),
								x.getPatients().geteMail(), 
								new HealthGroup(x.getPatients().getHealthGroup().getId(), x.getPatients().getHealthGroup().getName(),
										x.getPatients().getHealthGroup().getMinNormalPulse(),
										x.getPatients().getHealthGroup().getMaxNormalPulse(),
										x.getPatients().getHealthGroup().getServeyPeriod())),x.getDateTime())).collect(Collectors.toList());
	}

	private Stream<VisitOrm> getVisibleOrm(LocalDate beginDate, LocalDate endDate, Set<VisitOrm> records) {
		return records.stream().filter(x->(x.getDateTime().toLocalDate().isAfter(beginDate) && x.getDateTime().toLocalDate().isBefore(endDate)));
	}

	@Override
	public Iterable<Visit> getVisitsByDoctor(int doctorId, LocalDate beginDate, LocalDate endDate) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctorId);
		if (doctororm==null)
			return null;
		em.refresh(doctororm);
		Set<VisitOrm> records = doctororm.getVisitsDoctors();
		return getVisit(beginDate, endDate, records);
	}

	@Override
	public Iterable<Visit> getFreeVisits(int doctorId, LocalDate beginDate, LocalDate endDate) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, doctorId);
		if (doctororm==null)
			return null;
		em.refresh(doctororm);
		Set<VisitOrm> records = doctororm.getVisitsDoctors();
		return getVisibleOrm(beginDate, endDate, records).filter(x->x.getPatients()==null)
				.map(x->new Visit(new Doctor(x.getDoctors().getId(), x.getDoctors().getNameDoctor(),
						x.getDoctors().getPhoneNumberByDoctor(), x.getDoctors().geteMailDoctor()),
						new Patient(x.getPatients().getId(), x.getPatients().getName(), x.getPatients().getPhoneNumber(),
								x.getPatients().geteMail(), 
								new HealthGroup(x.getPatients().getHealthGroup().getId(), x.getPatients().getHealthGroup().getName(),
										x.getPatients().getHealthGroup().getMinNormalPulse(),
										x.getPatients().getHealthGroup().getMaxNormalPulse(),
										x.getPatients().getHealthGroup().getServeyPeriod())),x.getDateTime())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public String addPulseInfo(int patientId, LocalDateTime dateTime, int value) {
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm==null)
			return NO_PATIENT;
		HertBeatOrm pulse = new HertBeatOrm(patientorm, dateTime, value, patientorm.getHealthGroup().getServeyPeriod());
		em.persist(pulse);
		return OK;
	}

	@Override
	public Iterable<Integer> getPulseByPeriod(int patientId, LocalDate beginDate, LocalDate endDate, int surveyPeriod) {
		Set<Integer> res = new HashSet<>();
		List<LocalDate> pulseDate = em.createQuery("Select dateTime from HertBeatOrm",LocalDate.class).getResultList();
		PatientOrm patientorm = em.find(PatientOrm.class, patientId);
		if (patientorm==null)
			return null;
		em.refresh(patientorm);
		Set<HertBeatOrm> pulses = patientorm.getPulsePatients();
		if (pulseDate.contains(beginDate) && pulseDate.contains(endDate)) {
			Set<HertBeatOrm> pulsesbyDate = pulses.stream().filter(x->(x.getDateTime().toLocalDate().isAfter(beginDate)
					&& x.getDateTime().toLocalDate().isBefore(endDate))).collect(Collectors.toSet());
			for (HertBeatOrm iter:pulsesbyDate) {
				res.add(iter.getValue());
			}
		} else return null;
		
		return res;
	}

	@Override
	@Transactional
	public String replaceVisitsDoctor(int oldDoctorId, int newDoctorId, LocalDateTime beginDateTime,
			LocalDateTime endDateTime) {
		DoctorOrm doctororm = em.find(DoctorOrm.class, oldDoctorId);
		if (doctororm==null)
			return NO_DOCTOR;
		DoctorOrm newdoctororm = em.find(DoctorOrm.class, newDoctorId);
		if (newdoctororm==null)
			return NO_DOCTOR;
		em.refresh(doctororm);
		em.refresh(newdoctororm);
		Set<VisitOrm> visit = doctororm.getVisitsDoctors();
		Set<VisitOrm> newVisit = visit.stream().filter(x->(x.getDateTime().isAfter(beginDateTime) && x.getDateTime().isBefore(endDateTime))).collect(Collectors.toSet());
		return OK;
	}

	@Override
	@Transactional
	public String addHealthGroup(HealthGroup healthGroup) {
		if (em.find(HealthGroupOrm.class, healthGroup.getId())!=null) return RestResponseCode.ALREADY_EXIST;
		HealthGroupOrm healthGroupOrm = new HealthGroupOrm(healthGroup.getId(),healthGroup.getGroupName(),healthGroup.getMinNormalPulse(),healthGroup.getMaxNormalPulse(),healthGroup.getSurveyPeriod());
		em.persist(healthGroupOrm);
		return RestResponseCode.OK;
	}

	@Override
	@Transactional
	public String removeHealthGroup(int groupId) {
		HealthGroupOrm healtgrouporm = em.find(HealthGroupOrm.class, groupId);
		if (healtgrouporm==null)
			return NO_HEALTH_GROUP;
		em.refresh(healtgrouporm);
		Set<PatientOrm> patient = healtgrouporm.getPatient();
		patient.removeIf(x->x.getHealthGroup().getId()==groupId);
		em.remove(healtgrouporm);
		return OK;
	}

	@Override
	public Iterable<HealthGroup> getHealthGroups() {
		List<HealthGroupOrm> healthGroupOrm = em.createQuery("Select p from HealthGroupOrm p",HealthGroupOrm.class).getResultList();
		return healthGroupOrm.stream().
				map(x->new HealthGroup(x.getId(), x.getName(), x.getMinNormalPulse(), x.getMaxNormalPulse(), x.getServeyPeriod())).collect(Collectors.toList());
	}

	@Override
	public Iterable<Patient> getPatients() {
		List<PatientOrm> patientOrm = em.createQuery("Select p from PatientOrm",PatientOrm.class).getResultList();
		return patientOrm.stream().map(x->new Patient(x.getId(),
				x.getName(), x.getPhoneNumber(), x.geteMail(),
				new HealthGroup(x.getHealthGroup().getId(), x.getHealthGroup().getName(),
						x.getHealthGroup().getMinNormalPulse(),
						x.getHealthGroup().getMaxNormalPulse(), x.getHealthGroup().getServeyPeriod()))).collect(Collectors.toList());
	}

	@Override
	public Iterator<Doctor> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
