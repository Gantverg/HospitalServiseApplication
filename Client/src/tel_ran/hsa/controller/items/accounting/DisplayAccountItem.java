package tel_ran.hsa.controller.items.accounting;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.security.entities.Account;

public class DisplayAccountItem extends HospitalItem {

	@Override
	public String displayedName() {
		return "display account";
	}

	@Override
	public void perform() {
		String username = inputOutput.getString("Enter username");
		Account b = accountStream.getAccount(username);

		inputOutput.put(String.format("Account %s: Password: %s",b.getUsername(),b.getPassword()));
	}

}
