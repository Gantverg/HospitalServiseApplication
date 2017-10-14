package tel_ran.hsa.controller.items.accounting;

import tel_ran.hsa.controller.items.HospitalItem;

public class RemoveAccountItem extends HospitalItem {

	@Override
	public String displayedName() {
		return "Remove account";
	}

	@Override
	public void perform() {
		String username = inputOutput.getString("Enter username");
		inputOutput.put(accountStream.removeAccount(username));

	}

}
