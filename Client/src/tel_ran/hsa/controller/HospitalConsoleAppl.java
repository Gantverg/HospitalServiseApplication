package tel_ran.hsa.controller;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.model.interfaces.IHospital;
import tel_ran.hsa.utils.RestConfig;
import tel_ran.security.accounting.AccountStream;
import tel_ran.view.*;

public class HospitalConsoleAppl {

	
	private static final String adminAuth = "admin:admin";

	public static void main(String[] args) throws Exception {
		InputOutput inputOutput = new ConsoleInputOutput();

		RestConfig rest = new RestConfig(adminAuth);
		
		IHospital hospital = new WebClient(rest);
		HospitalItem.setInputOutput(inputOutput);
		HospitalItem.setHospital(hospital);
		HospitalItem.setRestConfig(rest);
		HospitalItem.setAccountStream(new AccountStream());
		(new Login()).perform();
	}
	
}
