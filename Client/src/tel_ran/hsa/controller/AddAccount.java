package tel_ran.hsa.controller;

import java.util.*;

import tel_ran.security.entities.Account;

public class AddAccount extends HospitalItem {

	@Override
	public String displayedName() {
		return "Add account";
	}

	@Override
	public void perform() {
		String username = inputOutput.getString("Enter username");
		String password = inputOutput.getString("Enter password");
		String[] roles = inputOutput.getString("Enter roles").split(",");
		Account account = new Account(username, password, new HashSet<String>(Arrays.asList(roles)));
		inputOutput.put(accauntStream.addAccount(account));
		//boolean b = accauntStream.addAccount(account);

		//inputOutput.put(String.format("Add account %s", b ? "was" : "wasn't"));
	}

}
