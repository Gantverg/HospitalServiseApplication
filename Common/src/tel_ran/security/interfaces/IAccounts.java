package tel_ran.security.interfaces;

import tel_ran.hsa.protocols.security.Roles;
import tel_ran.security.entities.*;

public interface IAccounts extends Iterable<Account>, Roles{
	
	String addAccount(Account account);
	String removeAccount(String username);
	String addRole(String username, String role);
	String removeRole(String username, String role);
	Account getAccount(String username);
	String[] getRoles(String username);
	String updatePassword(String username, String newPassword);
	boolean adminPresent();
	default boolean login() {
		return false;
	}
	
	default String[] getAllRoles() {
		String[] res = {ADMIN, MANAGER, DOCTOR, PATIENT, BEAT};
		return res;
	}
}
