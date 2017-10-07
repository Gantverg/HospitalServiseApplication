package tel_ran.security.interfaces;

import tel_ran.security.entities.*;

public interface IAccounts extends Iterable<Account>{
	
	String addAccount(Account account);
	String removeAccount(String username);
	String addRole(String username, String role);
	String removeRole(String username, String role);
	Account getAccount(String username);
	String updatePassword(String username, String newPassword);
	boolean adminPresent();
}
