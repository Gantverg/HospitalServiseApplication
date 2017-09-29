package tel_ran.hospital.controller;

import tel_ran.security.accounting.Account;


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
		Account account = new Account(username, password, roles);
		boolean b = accauntStream.addAccount(account);

		inputOutput.put(String.format("Add account %s", b ? "was" : "wasn't"));
	}

}
