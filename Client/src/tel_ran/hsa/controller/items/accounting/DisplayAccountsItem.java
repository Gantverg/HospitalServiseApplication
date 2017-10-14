package tel_ran.hsa.controller.items.accounting;

import tel_ran.hsa.controller.items.HospitalItem;

public class DisplayAccountsItem extends HospitalItem {

	@Override
	public String displayedName() {
		return "Display accounts";
	}

	@Override
	public void perform() {
		accountStream.forEach(inputOutput::put);
	}

}
