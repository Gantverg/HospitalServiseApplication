package tel_ran.security.accounting;

import java.io.*;
import java.util.*;

import tel_ran.hsa.protocols.api.RestResponseCode;
import tel_ran.hsa.protocols.security.Roles;
import tel_ran.security.entities.*;
import tel_ran.security.interfaces.IAccounts;

@SuppressWarnings("serial")
public class AccountsStream implements IAccounts, Serializable, RestResponseCode {

	private Map<String, Account> accounts;
	private String fileName;

	public AccountsStream(String fileName) {
		this.fileName = fileName;
	}

	public AccountsStream() {

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public Iterator<Account> iterator() {
		return accounts.values().iterator();
	}

	@Override
	public String addAccount(Account account) {
		if (accounts.putIfAbsent(account.getUsername(), account) == null)
			return OK;
		else
			return ALREADY_EXIST;
	}

	@Override
	public String removeAccount(String username) {
		if (accounts.remove(username) == null)
			return NO_ACCOUNT;
		else
			return OK;
	}

	@Override
	public String addRole(String username, String role) {
		Account account = accounts.get(username);
		Set<String> roles = account.getRoles();
		if(roles.add(role)) {
			account.setRoles(roles);
			return OK;
		} else {
			return ALREADY_EXIST;
		}
	}

	@Override
	public String removeRole(String username, String role) {
		Account account = accounts.get(username);
		Set<String> roles = account.getRoles();
		if(roles.remove(role)) {
			account.setRoles(roles);
			return OK;
		} else {
			return NO_ROLE;
		}
	}

	@Override
	public Account getAccount(String username) {
		return accounts.get(username);
	}

	@Override
	public String updatePassword(String username, String newPassword) {
		if (!accounts.containsKey(username)) {
			return NO_ACCOUNT;
		}
		accounts.get(username).setPassword(newPassword);
		return OK;
	}

	public boolean save() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))) {
			output.writeObject(accounts);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public void restore() {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))) {
			accounts = (Map<String, Account>) input.readObject();
		} catch (Exception e) {
		}
		if (accounts == null) {
			accounts = new HashMap<>();
		}
	}

	@Override
	public boolean adminPresent() {
		return accounts.values().stream().flatMap(account -> account.getRoles().stream())
				.anyMatch(role -> role.equals(Roles.ADMIN));
	}

	@Override
	public String[] getRoles(String username) {
		Account account = accounts.get(username);
		return account.getRolesAsArray();
	}

}
