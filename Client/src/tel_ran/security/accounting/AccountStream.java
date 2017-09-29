package tel_ran.security.accounting;

import java.util.Iterator;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tel_ran.rest.RestConfig;

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

	}

	public void save() {

	}

	@Override
	public Iterator<Account> iterator() {
		// TODO Auto-generated method stub
		return null;
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
