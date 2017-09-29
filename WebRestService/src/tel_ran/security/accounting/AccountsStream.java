package tel_ran.security.accounting;

import java.io.*;
import java.util.*;

import tel_ran.security.entities.*;
import tel_ran.security.interfaces.IAccounts;

@SuppressWarnings("serial")
public class AccountsStream implements IAccounts, Serializable {

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
	public boolean addAccount(Account account) {
		return accounts.putIfAbsent(account.getUsername(), account) == null;
	}

	@Override
	public boolean removeAccount(String username) {
		return accounts.remove(username) == null ? false : true;
	}

	@Override
	public boolean addRole(String username, String role) {
		Account account = accounts.get(username);
		Set<String> roles = account.getRoles();
		boolean isAdd = roles.add(role);
		account.setRoles(roles);
		return isAdd;
	}

	@Override
	public boolean removeRole(String username, String role) {
		Account account = accounts.get(username);
		Set<String> roles = account.getRoles();
		boolean isRemoved = roles.remove(role);
		account.setRoles(roles);
		return isRemoved;
	}

	@Override
	public Account getAccount(String username) {
		return accounts.get(username);
	}

	@Override
	public boolean updatePassword(String username, String newPassword) {
		if (!accounts.containsKey(username)) {
			return false;
		}
		accounts.get(username).setPassword(newPassword);
		return true;
	}

	public boolean save() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))){
			output.writeObject(accounts);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public void restore() {
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))) {
			accounts = (Map<String, Account>) input.readObject();
		} catch (Exception e) {
		}
		if (accounts == null) {
			accounts = new HashMap<>();
		}
	}

}
