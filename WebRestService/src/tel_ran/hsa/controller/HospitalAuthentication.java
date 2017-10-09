package tel_ran.hsa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;

import tel_ran.security.entities.*;
import tel_ran.security.interfaces.IAccounts;

//@Configuration
public class HospitalAuthentication implements UserDetailsService{
	
	@Autowired
	IAccounts accountsStream;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(!accountsStream.adminPresent()) {
			accountsStream.addAccount(new Account("admin", "admin", "ADMIN"));
		}
			
		Account account = accountsStream.getAccount(username);
		if(account == null)
			throw new UsernameNotFoundException("");
		return new User(username, account.getPassword(),
				AuthorityUtils.createAuthorityList(account.getRolesAsArray()));
	}

}
