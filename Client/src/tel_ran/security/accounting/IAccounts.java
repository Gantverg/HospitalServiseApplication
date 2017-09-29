package tel_ran.security.accounting;

public interface IAccounts extends Iterable<Account> {
	boolean addAccount(Account account);
	boolean removeAccount(String username);
	boolean addRole(String username, String role);
	boolean removeRole(String username, String role);
	Account getAccount(String username);
	boolean updatePassword(String username, String newPassword);
}
