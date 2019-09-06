package main.java.resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import main.java.auth.AuthenticationException;

/**
 * Created by sroca on 04/07/2017.
 */

public class Rest {
    /*
     * GET method
     */
    public String get (String urlToSend, String token) throws AuthenticationException {
    	int clientResponse = -1;
    	try {
    		urlToSend = urlToSend.replaceAll("\\+", "%2B");
    	} catch (Exception ex){
            ex.printStackTrace();
        }
        String response = "";
        try{
            URL url = new URL(urlToSend);

            System.out.println("GET: " + url);
            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();

            if (token != null) {
                String bearerAuth = "Bearer " + token;
                client.setRequestProperty("Authorization", bearerAuth);
            }
            
            client.setRequestMethod("GET");
            
            clientResponse = client.getResponseCode();
            if (clientResponse == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    response += line;
                }
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
        if(clientResponse == 401){
        	throw new AuthenticationException("Error", "");
        } 
        try {
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*
     * POST method
     */
    public String post (String urlToSend, String postData, String token) throws AuthenticationException {
        String response = "";
        int clientResponse = -1;
        try{
            URL url = new URL(urlToSend);

            System.out.println("POST: " + url + "     " + postData);
            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();

            if (token != null) {
                String bearerAuth = "Bearer " + token;
                client.setRequestProperty("Authorization", bearerAuth);
                client.setRequestProperty("Content-Type","application/json");
            } else {
            	try {
            		postData = postData.replaceAll("\\+", "%2B");
            	} catch (Exception ex){
                    ex.printStackTrace();
                }
                client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Length", "" + Integer.toString(postData.getBytes().length));

            client.setDoOutput(true);

            byte[] outputInBytes = postData.getBytes("UTF-8");
            OutputStream os = client.getOutputStream();
            os.write( outputInBytes );
            os.close();
            
            clientResponse = client.getResponseCode();
            if(clientResponse == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String line;
                while((line = bufferedReader.readLine()) != null){
                    response += line;
                }
                bufferedReader.close();
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
        if(clientResponse == 401){
        	throw new AuthenticationException("Authentication Error", "");
        }
        try {
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }
    
    /*
     * PUT method
     */
    public String put (String urlToSend, String postData, String token) throws AuthenticationException {
        String response = "";
        int clientResponse = -1;
        try{
            URL url = new URL(urlToSend);

            System.out.println("PUT: " + url + "     " + postData);
            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();

            if (token != null) {
                String bearerAuth = "Bearer " + token;
                client.setRequestProperty("Authorization", bearerAuth);
                client.setRequestProperty("Content-Type","application/json");
            } else {
            	try {
            		postData = postData.replaceAll("\\+", "%2B");
            	} catch (Exception ex){
                    ex.printStackTrace();
                }
                client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            client.setRequestMethod("PUT");
            client.setRequestProperty("Content-Length", "" + Integer.toString(postData.getBytes().length));

            client.setDoOutput(true);

            byte[] outputInBytes = postData.getBytes("UTF-8");
            OutputStream os = client.getOutputStream();
            os.write( outputInBytes );
            os.close();
            
            clientResponse = client.getResponseCode();
            if(clientResponse == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String line;
                while((line = bufferedReader.readLine()) != null){
                    response += line;
                }
                bufferedReader.close();
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
        if(clientResponse == 401){
        	throw new AuthenticationException("Authentication Error", "");
        }
        try {
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }
    
    
    /*
     * DELETE method
     */
    public String delete (String urlToSend, String postData, String token) throws AuthenticationException {
        String response = "";
        int clientResponse = -1;
        try{
            URL url = new URL(urlToSend);

            System.out.println("DELETE: " + url);
            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();

            if (token != null) {
                String bearerAuth = "Bearer " + token;
                client.setRequestProperty("Authorization", bearerAuth);
                client.setRequestProperty("Content-Type","application/json");
            } else {
            	try {
            		postData = postData.replaceAll("\\+", "%2B");
            	} catch (Exception ex){
                    ex.printStackTrace();
                }
                client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            client.setRequestMethod("DELETE");
            client.setRequestProperty("Content-Length", "" + Integer.toString(postData.getBytes().length));

            client.setDoOutput(true);

            byte[] outputInBytes = postData.getBytes("UTF-8");
            OutputStream os = client.getOutputStream();
            os.write( outputInBytes );
            os.close();

            clientResponse = client.getResponseCode();
            if(clientResponse == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String line;
                while((line = bufferedReader.readLine()) != null){
                    response += line;
                }
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
        if(clientResponse == 401){
        	throw new AuthenticationException("Authentication Error", "");
        }
        try {
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }
}
