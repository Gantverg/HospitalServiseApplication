package tel_ran.hsa.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import tel_ran.hsa.protocols.api.AccountRequest;
import tel_ran.hsa.protocols.api.RestRequest;
import tel_ran.hsa.protocols.security.Roles;
import tel_ran.security.entities.Account;


//@Configuration
public class HospitalAuthorization extends WebSecurityConfigurerAdapter {
	static Account account;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.httpBasic();
		http.csrf().disable();
	    http
          	.formLogin()
          		.loginPage(AccountRequest.LOGIN)
          		.permitAll()
          		.and()
          	.logout()
          		.permitAll();
		http.authorizeRequests().antMatchers(AccountRequest.ACCOUNTS+"/**")
			.hasRole(Roles.ADMIN);
		http.authorizeRequests().antMatchers(RestRequest.PATIENTS+"/**")//"+getId(Roles.PATIENT)+".*")
			.hasAnyRole(Roles.PATIENT,Roles.MANAGER);
		http.authorizeRequests().antMatchers(RestRequest.DOCTORS+"/**")//"+getId(Roles.DOCTOR)+".*")
			.hasAnyRole(Roles.DOCTOR,Roles.MANAGER);
		http.authorizeRequests().antMatchers(RestRequest.PULSE+"/**")//"+getId(Roles.DOCTOR)+".*")
		.hasAnyRole(Roles.BEAT,Roles.MANAGER);
		http.authorizeRequests().antMatchers(RestRequest.HEALTHGROUPS+"/**",RestRequest.VISITS+"/**")//"+getId(Roles.DOCTOR)+".*")
		.hasRole(Roles.MANAGER);
		
		http.authorizeRequests().anyRequest().permitAll();
	}

//	private String getId(String role) {
//		if(account.getRoles().contains(role))
//			return String.valueOf(account.getId());
//		else
//			return "";
//	}

}
