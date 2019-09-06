package main.java.objects;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.auth.AuthenticationException;
import main.java.resource.Rest;

/**
 * Created by sroca on 14/12/2017.
 */

public class Token {

    // Token data
	private String identif;
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private String id_token;
    
	// Server OAuth
	private String url_OAuth;
    private String url;
    private String nameDB;
    private String userName;
    private String password;

    public Token () {
    	identif = "";
    	access_token = "";
    	token_type = "";
    	expires_in = 0;
    	refresh_token = "";
    	id_token = "";
    	
        Properties misPropiedades = new Properties();
        try {
        	misPropiedades.load(new FileInputStream(System.getProperty("user.dir") + "/files/application.properties"));
        } catch (Exception e){ System.out.println("Ha ocurrido una excepcion al abrir el fichero, no se encuentra o esta protegido");}
    	
        url_OAuth = misPropiedades.getProperty("token.url");
        url = misPropiedades.getProperty("bbdd.url");
        nameDB = misPropiedades.getProperty("bbdd.name");
        userName = misPropiedades.getProperty("bbdd.username");
        password = misPropiedades.getProperty("bbdd.password");
    }

    public Token (String identif, String access_token, String token_type, int expires_in, String refresh_token, String id_token) {
    	this.identif = identif;
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.id_token = id_token;
    }

    public String getIdentif() {
        return identif;
    }

    public void setIdentif(String identif) {
        this.identif = identif;
    }
    
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
    
    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
    
    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
    
    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }
    
    /*
     * Function used to obtain the OAuth server token
     */
    public void getToken(String id, String secret) {
    	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	Rest rest = new Rest();
        Token token =  new Token();
        
        
        try {
            String response = rest.get(url_OAuth + "&client_id=" + id + "&client_secret=" + secret, null);
            if(response.length() > 0) {
            	response = response.replace("'", "\"");
            	token = mapper.readValue(response, Token.class);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        token.setIdentif(id);
        
    	this.identif = token.getIdentif();
        this.access_token = token.getAccess_token();
        this.token_type = token.getToken_type();
        this.expires_in = token.getExpires_in();
        this.refresh_token = token.getRefresh_token();
        this.id_token = token.getId_token();
    }

    /*
     * Function that obtains the token stored in the database
     */
    public void getTokenDB(String id) {
        String connect = "_connect";
        String auth = "_authenticate";
        String find = "_find";
        String insert = "_insert";
	    Rest rest = new Rest();
	    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    rest.post (url + connect, "server=localhost:27017", null);
	    rest.post (url + nameDB + auth, "username=" + userName + "&password=" + password, null);
	    String response = rest.get (url + nameDB + "tokens/" + find, null);
	
	    Token token = new Token();
	    if (!response.contains("{\"ok\": 1, \"results\": []")){
	        String json = getJson(response);
	
	        try {
	            token = mapper.readValue(json, Token.class);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } else {
	    	token.setIdentif(id);
	    	try {
				rest.post(url + nameDB + "tokens/" + insert, "docs=[" + mapper.writeValueAsString(token) + "]", null);
			} catch (AuthenticationException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
	    }
    	this.identif = token.getIdentif();
        this.access_token = token.getAccess_token();
        this.token_type = token.getToken_type();
        this.expires_in = token.getExpires_in();
        this.refresh_token = token.getRefresh_token();
        this.id_token = token.getId_token();
    }
    
    /*
     * Function that stores the token in the database
     */
    public void setTokenDB(String client_id) {
        String connect = "_connect";
        String auth = "_authenticate";
        String update = "_update";
	    Rest rest = new Rest();
	    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
        rest.post (url + connect, "server=localhost:27017", null);
        rest.post (url + nameDB + auth, "username=" + userName + "&password=" + password, null);
        String response = "";
		try {
			response = rest.post(url + nameDB + "tokens/" + update, "criteria={\"identif\":\"" + client_id + "\"}&newobj=" + mapper.writeValueAsString(this), null);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
    
    /*
     * Function that checks if the token has expired or not
     */
    public void checkToken(String client_id, String client_secret) {
        String[] partes = id_token.split("\\.");
        if (partes.length == 3) {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String info = new String(Base64.getDecoder().decode(partes[1]));
            InfoToken it = new InfoToken();
            try {
                it = mapper.readValue(info, InfoToken.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long now = System.currentTimeMillis()/1000;
            if (now > it.getExp()) {
                // It has expired
                do {
                    getToken(client_id, client_secret);
                } while (id_token.equals(""));
                setTokenDB(client_id);
            }
        }
    }
    
    /*
     * Function that obtains the object in JSON format and returns it as a string
     */
    private String getJson (String response) {
        String [] a = response.split("\"results\": \\[", 2);
        String [] aux = (a[1]).split("], \"id\":");
        return aux[0];
    }
    
}
