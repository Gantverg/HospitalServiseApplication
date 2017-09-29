package tel_ran.security.accounting;

public class Account {
	private String username;
	private String password;
	private String[] roles;

	public Account() {
	}

	public Account(String username, String password, String[] roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
		
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String[] getRoles() {
		return roles;
	}
}
