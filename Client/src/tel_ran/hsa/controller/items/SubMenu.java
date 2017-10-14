package tel_ran.hsa.controller.items;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.view.Item;
import tel_ran.view.Menu;

public abstract class SubMenu extends HospitalItem {
	private Iterable<Item> items;
	
	public void setItems(Iterable<Item> items) {
		this.items = items;
	}

	@Override
	public void perform() {
		Menu menu = new Menu(inputOutput, items);
		menu.runMenu();
	}

}
