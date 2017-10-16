package tel_ran.hsa.entities.jfx;

import javafx.beans.property.*;

public abstract class PersonJfx {
	IntegerProperty id;
	StringProperty name;
	StringProperty phoneNumber;
	StringProperty eMail;

	public PersonJfx() {
		this(0,"","","");
	}
	
	public PersonJfx(int id, String name, String phoneNumber, String eMail) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.phoneNumber = new SimpleStringProperty(phoneNumber);
		this.eMail = new SimpleStringProperty(eMail);
	}

	public int getId() {
		return id.get();
	}
	public String getName() {
		return name.get();
	}
	public String getPhoneNumber() {
		return phoneNumber.get();
	}
	public String geteMail() {
		return eMail.get();
	}
	public void setId(int id) {
		this.id.set(id);
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber.set(phoneNumber);
	}
	public void seteMail(String eMail) {
		this.eMail.set(eMail);
	}

	public IntegerProperty idProperty() {
		return id;
	}

	public StringProperty nameProperty() {
		return name;
	}

	public StringProperty phoneNumberProperty() {
		return phoneNumber;
	}
	public StringProperty eMailProperty() {
		return eMail;
	}
	
	@Override
	public String toString() {
		return "Person [Class=" + getClass().getName() + ", id=" + id + ", name=" + name + ", phoneNumber=" + phoneNumber
				+ ", eMail=" + eMail + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id.get();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonJfx other = (PersonJfx) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
