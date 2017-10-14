package tel_ran.hsa.controller.items.accounting;

import tel_ran.hsa.controller.items.HospitalItem;

public class SetPasswordItem extends HospitalItem {

	@Override
	public String displayedName() {
		return "Set password";
	}

	@Override
	public void perform() {
		String username = inputOutput.getString("Enter username");
		String password = inputOutput.getString("Enter password");
		inputOutput.put(accountStream.updatePassword(username, password));
	}

}
