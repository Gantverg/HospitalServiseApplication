package tel_ran.hsa.controller;

import tel_ran.hsa.model.interfaces.IHospital;
import tel_ran.hsa.utils.RestConfig;
import tel_ran.security.accounting.AccountStream;
import tel_ran.view.*;
import java.util.*;

public class HospitalConsoleAppl {

	
	private static final String adminAuth = "user:8f58939b-304f-4ad4-a342-37ea934e242e";//"admin:admin";

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
	}

	private static Menu createMenu(InputOutput inputOutput) {
		Item[] items = { 
				//new AddAccount(), 
				//new GetAccount(),
				new AddDoctor(),
				new AddPatient(),
				new GetDoctor(),
				new GetPatient(),
				new RemoveDoctor(),
				new RemovePatient(),
				new UpdateDoctor(),
				new UpdatePatient(),
				new AddHealthGroup(),
				new RemoveHealthGroup(),
				new GetHealthGroups(),
				new BuildShedule(),
				new BookVisit(),
				new GetVisitsByDoctor(),
				new GetVisitsByPatient(),
				new GetFreeVisits(),
				new CancelVisit(),
				new GetDoctorPatients(),
				new GetPatientDoctors(),
				new GetPulseByPeriod(),
				new ExitItem() };
		return new Menu(inputOutput, Arrays.asList(items));
	}

}
