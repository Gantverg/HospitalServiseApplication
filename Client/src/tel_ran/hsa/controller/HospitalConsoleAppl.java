package tel_ran.hsa.controller;

import tel_ran.hsa.model.interfaces.IHospital;
import tel_ran.hsa.utils.RestConfig;
import tel_ran.security.accounting.AccountStream;
import tel_ran.view.*;
import java.util.*;

public class HospitalConsoleAppl {

	
	private static final String adminAuth = "manager:manager";//"admin:admin";

	public static void main(String[] args) throws Exception {
		InputOutput inputOutput = new ConsoleInputOutput();

		RestConfig rest = new RestConfig(adminAuth);
		
		IHospital hospital = new WebClient(rest);
		HospitalItem.setInputOutput(inputOutput);
		HospitalItem.setHospital(hospital);
		HospitalItem.setRestConfig(rest);
		HospitalItem.setAccauntStream(new AccountStream());
		Menu menu = createMenu(inputOutput);
		menu.runMenu();
		//Menu menu = loginMenu(inputOutput);
		//menu.runLogin();
		//createMenu(inputOutput, menu);
		//menu.runMenu();
	}
	
	private static Menu loginMenu(InputOutput inputOutput) {
		List<Item> items = new ArrayList<>();
		items.add(new Login());
		return new Menu(inputOutput, items);
	}

	private static Menu createMenu(InputOutput inputOutput) {
		Item[] items = { 
				new AddDoctor(),
				new  AddPatient(),
				new AddHealthGroup(),
				new RemoveDoctor(),
				new RemovePatient(),
				new RemoveHealthGroup(),
				new UpdateDoctor(),
				new UpdatePatient(),
				new SetHealthGroup(),
				new SetTimeSlot(),
				new BookVisit(),
				new CancelVisit(),
				new ReplaceVisitsDoctor(),
				new BuildShedule(),
				new GetDoctor(),
				new GetPatient(),
				new GetHealthGroup(),
				new GetDoctors(),
				new GetPatients(),
				new GetHealthGroups(),
				new GetVisits(),
				new GetDoctorPatients(),
				new GetPatientDoctors(),
				new GetVisitsByPatient(),
				new GetVisitsByDoctor(),
				new GetFreeVisits(),
				new ExitItem() };
		return new Menu(inputOutput, Arrays.asList(items));
	}

}
