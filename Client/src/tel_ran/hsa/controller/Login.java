package tel_ran.hsa.controller;

import java.util.Arrays;

import tel_ran.hsa.controller.items.*;
import tel_ran.hsa.controller.items.accounting.*;
import tel_ran.hsa.controller.items.data.*;
import tel_ran.hsa.controller.items.reports.*;
import tel_ran.hsa.controller.items.visiting.*;
import tel_ran.view.ExitItem;
import tel_ran.view.Menu;

public class Login extends HospitalItem{

	@Override
	public String displayedName() {
		return "Login";
	}

	@Override
	public void perform() {
		int count = 0;
		while (count++<4) {
			String username = inputOutput.getString("Enter username");
			String password = inputOutput.getString("Enter password");
			String tokenStr = username + ":" + password;
			restConfig.setUserPass(tokenStr);
			((WebClient)hospital).setRestConfig(restConfig);
			accountStream.setRest(restConfig);
			
			if (accountStream.login()) {
				runMenu();
				break;
			}
			else
				inputOutput.put("wrong user or password");
		}
	}

	private void runMenu() {
		SubMenu dataSubmenu = new DataSubmenu();
		dataSubmenu.setItems(Arrays.asList(new AddDoctor(),
										   new AddPatient(),
										   new AddHealthGroup(),
										   new RemoveDoctor(),
										   new RemovePatient(),
										   new RemoveHealthGroup(),
										   new SetHealthGroup(),
										   new SetTherapist(),
										   new SetTimeSlot(),
										   new UpdateDoctor(),
										   new UpdatePatient(),
										   new ExitItem()));
		SubMenu reportsSubmenu = new ReportsSubmenu();
		reportsSubmenu.setItems(Arrays.asList(new GetDoctors(),
										   new GetDoctor(),
										   new GetPatients(),
										   new GetPatient(),
										   new GetHealthGroups(),
										   new GetHealthGroup(),
										   new GetPatientDoctors(),
										   new GetDoctorPatients(),
										   new GetPulseByPeriod(),
										   new GetPulseBySurveyPeriod(),
										   new ExitItem()));
		SubMenu visitingSubmenu = new VisitingSubmenu();
		visitingSubmenu.setItems(Arrays.asList(new BuildSchedule(),
										   new BuildScheduleByDoctor(),
										   new BookVisit(),
										   new CancelVisit(),
										   new ReplaceVisitsDoctor(),
										   new GetVisits(),
										   new GetVisitsByDoctor(),
										   new GetVisitsByPatient(),
										   new GetFreeVisits(),
										   new ExitItem()));
		SubMenu accountingSubmenu = new AccountingSubmenu();
		accountingSubmenu.setItems(Arrays.asList(new Login(),
										   new AddAccountItem(),
										   new RemoveAccountItem(),
										   new AddRoleItem(),
										   new RemoveRoleItem(),
										   new SetPasswordItem(),
										   new DisplayAccountsItem(),
										   new DisplayAccountItem(),
										   new ExitItem()));
		Menu menu = new Menu(inputOutput, Arrays.asList(dataSubmenu,
														reportsSubmenu,
														visitingSubmenu,
														accountingSubmenu,
														new ExitItem()));
		menu.runMenu();
	}

}
