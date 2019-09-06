package main.java.core;

import main.java.auth.AuthenticationException;
import main.java.objects.Token;
import main.java.resource.Rest;

public class Deregistration {

	private String urlToSend;
	private String postData;
	private Token token;
	
	public Deregistration (String urlToSend, String id, Token token) {
		this.urlToSend = urlToSend + "?client_id=" + id;
		this.postData = "";
		this.token = token;
		
		run();
	}
	
	private void run () {
		
		Rest rest = new Rest();
		
		try {
			rest.delete(urlToSend, postData, token.getId_token());
			
	    } catch (AuthenticationException ex){
	        ex.printStackTrace();
	    }
	}
	
}
