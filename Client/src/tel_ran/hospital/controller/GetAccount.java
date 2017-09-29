package tel_ran.hospital.controller;

import java.util.Map;

import tel_ran.security.accounting.Account;

public class GetAccount extends HospitalItem {

	@Override
	public String displayedName() {
		// TODO Auto-generated method stub
		return "get account";
	}

	@Override
	public void perform() {
		String username = inputOutput.getString("Enter username");
		Account b = accauntStream.getAccount(username);

		inputOutput.put(String.format("Account %s: Password: %s",b.getUsername(),b.getPassword()));
	}

}
