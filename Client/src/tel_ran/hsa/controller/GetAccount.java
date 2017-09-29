package tel_ran.hsa.controller;

import tel_ran.security.entities.Account;

public class GetAccount extends HospitalItem {

	@Override
	public String displayedName() {
		return "get account";
	}

	@Override
	public void perform() {
		String username = inputOutput.getString("Enter username");
		Account b = accauntStream.getAccount(username);

		inputOutput.put(String.format("Account %s: Password: %s",b.getUsername(),b.getPassword()));
	}

}
