package tel_ran.hsa.controller.items.accounting;

import java.util.HashSet;
import java.util.Set;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.protocols.security.Roles;
import tel_ran.security.entities.Account;

public class AddAccountItem extends HospitalItem implements Roles{

	@Override
	public String displayedName() {
		return "Add new account";
	}

	@Override
	public void perform() {
		String username = inputOutput.getString("Enter username");
		String password = inputOutput.getString("Enter password");
		Set<String> roles = new HashSet<>();
		roles.add(inputOutput.getString("Enter account role", accountStream.getAllRoles()));
		Account account = new Account(username, password, roles);
		
		inputOutput.put(accountStream.addAccount(account));
	}
}
