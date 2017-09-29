package tel_ran.hospital.entities.not_use;

abstract class Person {
	int id;
	String name;
	String phoneNumber;
	String eMail;
	
	public Person(int id, String name, String phoneNumber, String eMail) {
		super();
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.eMail = eMail;
	}
	
	public Person() {
		
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String geteMail() {
		return eMail;
	}

}
