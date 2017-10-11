package tel_ran.hsa.controller;

import java.util.LinkedList;
import java.util.List;

import tel_ran.hsa.protocols.api.RestRequest;
import tel_ran.view.ExitItem;
import tel_ran.view.Item;
import tel_ran.view.Menu;

public class Login extends HospitalItem{

	/*private static final String ADD_ACCOUNT = "add_account";
	private static final String GET_ACCOUNT = "get_account";
	private static final String ADD_DOCTOR = "add_doctor";
	private static final String ADD_PATIENT = "add_patient";
	private static final String ADD_HEALTH_GROUP = "add_health_group";
	private static final String BUILD_SHEDULE = "build_shedule";
	private static final String BOOK_VISIT = "book_visit";
	private static final String CANCEL_VISIT = "cancel_visit";
	
	private static final String REMOVE_DOCTOR = "remove_doctor";
	private static final String REMOVE_HEALTH_GROUP = "remove_health_group";
	private static final String GET_DOCTOR_PATIENTS = "get_doctors_patients";
	private static final String GET_PATIENT_DOCTORS = "get_patient_doctors";
	private static final String GET_VISITS_BY_DOCTOR = "get_visits_by_doctor";
	private static final String GET_VISITS_BY_PATIENT = "get_visits_by_patient";
	private static final String GET_PULSE_BY_PERIOD = "get_pulse_by_period";
	private static final String UPDATE_DOCTOR = "update_doctor";
	private static final String UPDATE_PATIENT = "update_patient";
	private static final String GET_DOCTORS = "get_doctors";
	private static final String GET_PATIENTS = "get_patients";
	private static final String GET_HEALTH_GROUPS = "get_health_groups";
	private static final String GET_VISITS = "get_visits";
	private static final String REMOVE_PATIENT = "remove_patient";
	private static final String GET_DOCTOR = "get_doctor";
	private static final String GET_PATIENT = "get_patient";
	private static final String GET_HEALTH_GROUP = "get_health_group";
	private static final String SET_TIME_SLOT = "set_time_slot";
	private static final String SET_HEALTH_GROUP = "set_health_group";
	private static final String GET_FREE_VISITS = "get_free_visits";
	private static final String GET_PULSE_BY_SURVEY_PERIOD = "get_pulse_by_survey_period";
	private static final String REPLACE_VISITS_DOCTOR = "replace_visits_doctor";*/
	

	@Override
	public String displayedName() {
		
		return "Login";
	}

	@Override
	public void perform() {
		// TODO Auto-generated method stub
		String username = inputOutput.getString("Enter username");
		String password = inputOutput.getString("Enter password");
		List<Item> items = new LinkedList<>();
		String tokenStr = username + ":" + password;
		restConfig.setUserPass(tokenStr);
		WebClient.headers=restConfig.headers;
		items.add(new AddDoctor());
		items.add(new AddPatient());
		items.add(new AddHealthGroup());
		items.add(new RemoveDoctor());
		items.add(new RemovePatient());
		items.add(new RemoveHealthGroup());
		items.add(new UpdateDoctor());
		items.add(new UpdatePatient());
		items.add(new SetHealthGroup());
		items.add(new SetTimeSlot());
		items.add(new BookVisit());
		items.add(new CancelVisit());
		items.add(new ReplaceVisitsDoctor());
		items.add(new BuildShedule());
		items.add(new GetDoctor());
		items.add(new GetPatient());
		items.add(new GetHealthGroup());
		items.add(new GetDoctors());
		items.add(new GetPatients());
		items.add(new GetHealthGroups());
		items.add(new GetVisits());
		items.add(new GetDoctorPatients());
		items.add(new GetPatientDoctors());
		items.add(new GetVisitsByPatient());
		items.add(new GetVisitsByDoctor());
		items.add(new GetFreeVisits());
		items.add(new ExitItem());




		










		


	
		/*Iterable<String> methods = accauntStream.getMethodsForRole(username);
		
		items.add(new Login());
		for(String s : methods){
			switch(s){
				case ADD_ACCOUNT:items.add(new AddAccount());break;
				case GET_ACCOUNT:items.add(new GetAccount());break;
				
				case ADD_DOCTOR:items.add(new AddDoctor());break;
				case ADD_PATIENT:items.add(new AddPatient());break;
				case ADD_HEALTH_GROUP:items.add(new AddHealthGroup());break;
				
				case REMOVE_DOCTOR:items.add(new RemoveDoctor());break;
				case REMOVE_PATIENT:items.add(new RemovePatient());break;
				case REMOVE_HEALTH_GROUP:items.add(new RemoveHealthGroup());break;
				
				case UPDATE_DOCTOR:items.add(new UpdateDoctor());break;
				case UPDATE_PATIENT:items.add(new UpdatePatient());break;
				
				case GET_DOCTOR:items.add(new GetDoctor());break;
				case GET_PATIENT:items.add(new GetPatient());break;
				case GET_HEALTH_GROUP:items.add(new GetHealthGroup());break;


				case GET_DOCTORS:items.add(new GetDoctors());break;
				case GET_PATIENTS:items.add(new GetPatients());break;
				case GET_HEALTH_GROUPS:items.add(new GetHealthGroups());break;
				case GET_VISITS:items.add(new GetVisits());break;

				case SET_TIME_SLOT:items.add(new SetTimeSlot());break;
				case SET_HEALTH_GROUP:items.add(new SetHealthGroup());break;
				
				case REPLACE_VISITS_DOCTOR:items.add(new ReplaceVisitsDoctor());break;
				case BUILD_SHEDULE:items.add(new BuildShedule());break;
				case BOOK_VISIT:items.add(new BookVisit());break;
				case CANCEL_VISIT:items.add(new CancelVisit());break;
				
				
				case GET_PATIENT_DOCTORS:items.add(new GetPatientDoctors());break;
				case GET_DOCTOR_PATIENTS:items.add(new GetDoctorPatients());break;
				case GET_VISITS_BY_PATIENT:items.add(new GetVisitsByPatient());break;
				case GET_VISITS_BY_DOCTOR:items.add(new GetVisitsByDoctor());break;
				case GET_FREE_VISITS:items.add(new GetFreeVisits());break;
				
				case GET_PULSE_BY_PERIOD:items.add(new GetPulseByPeriod());break;
				case GET_PULSE_BY_SURVEY_PERIOD:items.add(new GetPulseBySurveyPeriod());break;

				


			}
		}
		
		items.add(new ExitItem());
		Menu.setItems(items);*/
      // Menu.setItems(items);
	}

}
