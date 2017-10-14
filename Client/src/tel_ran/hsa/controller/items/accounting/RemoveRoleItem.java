package tel_ran.hsa.controller.items.accounting;

import tel_ran.hsa.controller.items.HospitalItem;

public class RemoveRoleItem extends HospitalItem {

	@Override
	public String displayedName() {
		return "Remove role";
	}

	@Override
	public void perform() {
		String username = inputOutput.getString("Enter username");
		String role = inputOutput.getString("Enter role",accountStream.getAllRoles());
		inputOutput.put(accountStream.removeRole(username, role));
	}

}
