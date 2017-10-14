package tel_ran.security.accounting;

import java.util.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import tel_ran.hsa.protocols.api.AccountRequest;
import tel_ran.hsa.utils.RestConfig;
import tel_ran.security.entities.Account;
import tel_ran.security.interfaces.IAccounts;

public class AccountStream implements IAccounts,AccountRequest {

	private RestConfig rest;
	private RestTemplate restTemplate;
	private String URL;
	private HttpHeaders headers;
	
	public AccountStream(RestConfig rest) {
		super();
		setFieds(rest);
	}
	
	private void setFieds(RestConfig newRest) {
		restTemplate = newRest.restTemplate;
		URL = newRest.URL;
		headers = newRest.headers;
	}

	public RestConfig getRest() {
		return rest;
	}

	public void setRest(RestConfig newRest) {
		this.rest = newRest;
		setFieds(rest);
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

	@Override
	public Iterator<Account> iterator() {
		return account.values().iterator();
	}

	@Override
	public  String addAccount(Account account) {
		HttpEntity<Account> requestEntity = new HttpEntity<>(account,headers);
		ResponseEntity<String> response = restTemplate.exchange(URL +ACCOUNTS, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<String>() {});
		return response.getBody();
	}

	public boolean login() {
		try {
			HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
			@SuppressWarnings("unused")
			ResponseEntity<String> response = restTemplate.exchange(URL + LOGIN, HttpMethod.GET,
					requestEntity, new ParameterizedTypeReference<String>() {});
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public String saveAccounts()
	{
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + ACCOUNTS, HttpMethod.PUT,
				requestEntity, new ParameterizedTypeReference<String>() {});
		return response.getBody();
	}
	@Override
	public String updatePassword(String userName, String password)
	{
		Map<String,String> map = new HashMap<>();
		map.put("password", password);
		HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(map,headers);
		ResponseEntity<String> response = restTemplate.exchange(URL +ACCOUNTS+"/"+userName, HttpMethod.PUT,
				requestEntity, new ParameterizedTypeReference<String>() {});
		return response.getBody();
	}
	@Override
	public String removeAccount(String userName)
	{
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(URL +ACCOUNTS+"/"+userName, HttpMethod.DELETE,
				requestEntity, new ParameterizedTypeReference<String>() {});
		return response.getBody();
	}
	
	@Override
	public String[] getRoles(String userName)
	{
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String[]> response = restTemplate.exchange(URL +ACCOUNTS+"/"+userName+ROLES, HttpMethod.GET,
				requestEntity, new ParameterizedTypeReference<String[]>() {});
		return response.getBody();
	}
	
	@Override
	public Account getAccount(String username) {
		HttpEntity<String> requestEntity = new HttpEntity<>(username,rest.headers);
		ResponseEntity<Account> response = rest.restTemplate.exchange(URL + ACCOUNTS , HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<Account>() {
				});
		return response.getBody();
	}

	/*public Iterable<String> getMethodsForRole(String username) {
		HttpEntity<String> requestEntity = new HttpEntity<>(username,rest.headers);
		ResponseEntity<Iterable<String>> response = rest.restTemplate.exchange(rest.URL + GET_METHODS_FOR_ROLE, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<Iterable<String>>() {
				});
		return response.getBody();
	}*/
	@Override
	public String addRole(String username, String role) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(URL +ACCOUNTS+"/"+username+ROLES+"/"+role, HttpMethod.PUT,
				requestEntity, new ParameterizedTypeReference<String>() {});
		return response.getBody();
	}
	@Override
	public String removeRole(String username, String role) {
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(URL +ACCOUNTS+"/"+username+ROLES+"/"+role, HttpMethod.DELETE,
				requestEntity, new ParameterizedTypeReference<String>() {});
		return response.getBody();
	}
	@Override
	public boolean adminPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	

}
