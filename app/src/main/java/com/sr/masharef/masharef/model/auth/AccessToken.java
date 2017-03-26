package com.sr.masharef.masharef.model.auth;

import java.util.HashMap;

public class AccessToken {

	private String accountId;
	private String token;
	
	public AccessToken(HashMap<String, String> info){
		super();
		
		if(info!=null){
			accountId = info.get("userId");
			token = info.get("accessToken");
		}
		
	}
	
	public AccessToken(String accountId, String token){
		super();
		
		this.accountId = accountId;
		this.token 	   = token;	
		
	}
	
	public String getAccountId() {
		return accountId;
	}

	public String getToken() {
		return token;
	}
	
	public boolean isValid(){
		return (accountId!=null && accountId.trim().length()>0);
	}
	

}
