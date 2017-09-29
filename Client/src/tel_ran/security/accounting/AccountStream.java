package tel_ran.security.accounting;

import java.util.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import tel_ran.hsa.utils.RestConfig;
import tel_ran.security.entities.Account;
import tel_ran.security.interfaces.IAccounts;

public class AccountStream implements IAccounts {

	RestConfig rest;

	public RestConfig getRest() {
		return rest;
	}

	public void setRest(RestConfig rest) {
		this.rest = rest;
	}

	Map<String, Account> account;
	String filePath;

	public AccountStream() {

	}

	public AccountStream(String filePath) {
		this.filePath = filePath;
	}

	public Map<String, Account> getAccount() {
		return account;
	}

	public String getFilePath() {
		return filePath;
	}

	public void restore() {
		// TODO Auto-generated method stub
	}

	public void save() {
		// TODO Auto-generated method stub
	}

	@Override
	public Iterator<Account> iterator() {
		return account.values().iterator();
	}

	@Override
	public boolean addAccount(Account account) {
		HttpEntity<Account> requestEntity = new HttpEntity<>(account, rest.headers);
		ResponseEntity<Boolean> response = rest.restTemplate.exchange(rest.URL + "/account/add", HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<Boolean>() {
				});
		return response.getBody();
	}

	@Override
	public boolean removeAccount(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addRole(String username, String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRole(String username, String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getAccount(String username) {
		HttpEntity<String> requestEntity = new HttpEntity<>(username,rest.headers);
		ResponseEntity<Account> response = rest.restTemplate.exchange(rest.URL + "/account/get" , HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<Account>() {
				});
		return response.getBody();
	}

	@Override
	public boolean updatePassword(String username, String newPassword) {
		// TODO Auto-generated method stub
		return false;
	}

}
