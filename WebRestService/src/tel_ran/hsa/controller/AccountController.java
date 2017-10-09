package tel_ran.hsa.controller;

import java.util.HashSet;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.*;

import tel_ran.hsa.protocols.api.AccountRequest;
import tel_ran.security.entities.Account;
import tel_ran.security.interfaces.IAccounts;
import tel_ran.security.accounting.AccountsStream;

@RestController
//@ImportResource({"classpath:security.xml"})
public class AccountController {
	//@Autowired
	IAccounts accounts;
	
	@RequestMapping(value = AccountRequest.LOGIN, method = RequestMethod.GET)
	public void getLoginResponse() {}
	
	@RequestMapping(value = AccountRequest.ACCOUNTS, method = RequestMethod.POST)
	public String addAccount(@RequestBody Account account) {
		return accounts.addAccount(account);
	}
	
	@RequestMapping(value = AccountRequest.ACCOUNTS, method = RequestMethod.PUT)
	public boolean saveAccounts() {
		return ((AccountsStream) accounts).save();
	}
			
	@RequestMapping(value = AccountRequest.ACCOUNTS+"/{"+AccountRequest.USERNAME+"}", method = RequestMethod.DELETE)
	public String removeAccount(@PathVariable String username) {
		return accounts.removeAccount(username);
	}

	@RequestMapping(value = AccountRequest.ACCOUNTS+"/{"+AccountRequest.USERNAME+"}"+
						    AccountRequest.ROLES+"/{"+AccountRequest.ROLE+"}", method = RequestMethod.PUT)
	public String addRole(@PathVariable String username, @PathVariable String role) {
		return accounts.addRole(username, role);
	}
	
	@RequestMapping(value = AccountRequest.ACCOUNTS+"/{"+AccountRequest.USERNAME+"}"+
		    				AccountRequest.ROLES+"/{"+AccountRequest.ROLE+"}", method = RequestMethod.DELETE)
	public String removeRole(@PathVariable String username, @PathVariable String role) {
		return accounts.removeRole(username, role);
	}
	
	@RequestMapping(value = AccountRequest.ACCOUNTS+"/{"+AccountRequest.USERNAME+"}", method = RequestMethod.PUT)
	public String updatePassword(@PathVariable String username, 
								  @RequestBody String newPassword) {
		return accounts.updatePassword(username, newPassword);
	}

	@RequestMapping(value = AccountRequest.ACCOUNTS, method = RequestMethod.GET)
	public Iterable<Account> getAccounts() {
		return StreamSupport.stream(accounts.spliterator(), false).collect(Collectors.toList());
	}

	@RequestMapping(value = AccountRequest.ACCOUNTS+"/{"+AccountRequest.USERNAME+"}"+
		    				AccountRequest.ROLES, method = RequestMethod.GET)
	public Iterable<String> getRoles(@PathVariable String username) {
		Account account = accounts.getAccount(username);
		return account == null ? new HashSet<String>() : account.getRoles();
	}

}
