package main.java.core;

import java.util.ArrayList;

import main.java.auth.AuthenticationException;
import main.java.objects.Token;
import main.java.resource.Rest;

public class Registration {

	private String urlToSend;
	private String postData;
	private Token token;
	
	public Registration(String urlToSend, String client_id, String message, boolean menu, String address, ArrayList<String> canUse, Token token) {
		this.urlToSend = urlToSend;
		
		String use = "";
		int i;
		for (i = 0; i < (canUse.size() - 1); i++)
			use = use + "\"" + canUse.get(i) + "\",";
		if(canUse.size()>0)
			use = use + "\"" + canUse.get(i) + "\"";
		
		this.postData = "{\"client_id\":\"" + client_id + "\",\"message\":\"" + message +"\",\"menu\":" + menu + ",\"address\":\"" + address + "\",\"canUse\":[" + use +"]}";
		
		this.token = token;
	}
	
	public boolean run() {
		
		Rest rest = new Rest();
		
        try{ 	
        	rest.post(urlToSend, postData, token.getId_token());
        
        } catch (AuthenticationException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
	}

}
