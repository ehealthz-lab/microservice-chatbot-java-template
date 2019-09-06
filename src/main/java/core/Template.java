package main.java.core;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

import main.java.objects.Token;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class Template {

	private static Server server;
    private static Token token;
    private static String deleteFunctionality;
    private static String client_id;
    private static String client_secret;
    
	public static void main(String[] args) throws InterruptedException {

        Signal.handle(new Signal("TERM"), new SignalHandler() {
            public void handle(Signal sig) {
            	// Stopping the server
    			server.stopServer();
    			
    			token.getTokenDB(client_id);
    	        
    	        // It checks if the token has expired
    	        token.checkToken(client_id, client_secret);
    	        
    	        if (token.getId_token().equals("")) {
    		        do {
    				token.getToken(client_id, client_secret);
    				} while (token.getId_token().equals(""));
    		        token.setTokenDB(client_id);
    	        }
    			new Deregistration(deleteFunctionality, format(client_id), token);
    			
                System.out.format("\nFIN PROGRAMA\n");
                System.exit(0);
            }
        });
        
        Properties misPropiedades = new Properties();
        try {
        	misPropiedades.load(new FileInputStream(System.getProperty("user.dir") + "/files/application.properties"));
        } catch (Exception e){ System.out.println("Ha ocurrido una excepcion al abrir el fichero, no se encuentra o esta protegido");}
        
		client_id = misPropiedades.getProperty("token.client_id");
		client_secret = misPropiedades.getProperty("token.client_secret");
		
        // It obtains the token from the database
        token = new Token();
        token.getTokenDB(client_id);
        
        // It checks if the token has expired
        token.checkToken(client_id, client_secret);
        
        if (token.getId_token().equals("")) {
	        do {
			token.getToken(client_id, client_secret);
			} while (token.getId_token().equals(""));
	        token.setTokenDB(client_id);
        }
		
		String newFunctionality = misPropiedades.getProperty("botCore.newFunctionality");
		deleteFunctionality = misPropiedades.getProperty("botCore.deleteFunctionality");
		String message = format(misPropiedades.getProperty("bbdd.mesage")).replaceAll("\\+", "%20");;
		boolean menu = true;
		String addrr = misPropiedades.getProperty("bbdd.addrr");
		ArrayList<String> canUse = new ArrayList<String>();
		canUse.add(misPropiedades.getProperty("bbdd.canUse"));
		
		// HTTPS connection
		System.setProperty("javax.net.ssl.trustStore", misPropiedades.getProperty("cacerts.file"));
		System.setProperty("javax.net.ssl.trustStorePassword", misPropiedades.getProperty("cacerts.password"));
		
		// It registers the microservice in the API gateway
		boolean run = new Registration(newFunctionality, client_id, message, menu, addrr, canUse, token).run();
		
		if (run) {
			// Run the server
			server = new Server();
			server.startServer(misPropiedades.getProperty("server.addrr"));
			
	        while(true) {
	            Thread.sleep(500);
	        }
		} else
			System.out.println("ERROR: No se ha podido realizar el registro");

	}
	
    /*
     * Function that put the String s in URL format
     */
    private static String format (String s) {
    	String result = "";
    	
        try {
			result = java.net.URLDecoder.decode(s, java.nio.charset.StandardCharsets.UTF_8.toString());
			s = java.net.URLDecoder.decode(result, java.nio.charset.StandardCharsets.UTF_8.toString());
			result = java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        result = result.replaceAll("%5Cn", "%0A");
    	System.out.println("FORMAT: " + result);
    	return result;
    }
    
}
