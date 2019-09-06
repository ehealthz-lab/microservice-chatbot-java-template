package main.java.resource;

import main.java.auth.AuthenticationException;
import main.java.objects.Data;
import main.java.objects.Functionality;
import main.java.objects.Notification;
import main.java.objects.Person;
import main.java.objects.Data.Message;
import main.java.objects.Request;
import main.java.objects.Token;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.Nodemapper;
import org.alicebot.ab.ParseState;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import main.java.auth.SecurityFilter;
import main.java.auth.SecurityFilter.User;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

/**
 *
 * @author Surya Roca
 */
@Path("/")
@Consumes("application/json")
@Produces("application/json")
public class Resources {
    
	// API gateway
	private String url_botCore;
    
    private String nameService;
	
    private int pause;
    
	private String client_id;
	private String client_secret;
	
	private Token token;
	
	private String notification;

	/*
	 * Class constructor
	 */
	public Resources () {
		
        Properties misPropiedades = new Properties();
        try {
        	misPropiedades.load(new FileInputStream(System.getProperty("user.dir") + "/files/application.properties"));
        } catch (Exception e){ System.out.println("Ha ocurrido una excepcion al abrir el fichero, no se encuentra o esta protegido");}
		
        url_botCore = misPropiedades.getProperty("botCore.url");
        nameService = misPropiedades.getProperty("bbdd.mesage");
        pause = Integer.parseInt(misPropiedades.getProperty("resources.pause"));
        client_id = misPropiedades.getProperty("token.client_id");
        client_secret = misPropiedades.getProperty("token.client_secret");
        notification = misPropiedades.getProperty("resources.notification");
        
		token = new Token();
		
        // It obtains the token from the database
        token.getTokenDB(client_id);
        
        // It checks if the token has expired
        token.checkToken(client_id, client_secret);
        
        if (token.getId_token().equals("")) {
	        do {
	        	try {
     				Thread.sleep(250);
     			} catch (InterruptedException e) {
     				e.printStackTrace();
     			}
	        	token.getToken(client_id, client_secret);
			} while (token.getId_token().equals(""));
	        token.setTokenDB(client_id);
        }
    }
    
	@POST
	@Path("shutdown")
	public void receive_shutdown(@HeaderParam("Authorization") String authorization, String body, @Suspended final AsyncResponse asyncResponse){
		new Thread(new Runnable() {
            @Override
            public void run() {
            	User auth = new SecurityFilter().authenticate(authorization);
		     	if (auth == null) {
		     		asyncResponse.resume(Response.status(Status.UNAUTHORIZED).build());
		                 return;
		     	}
		     	System.exit(0);
		     	asyncResponse.resume("OK");
            }
		}).start();
	}
	
	@GET
	@Path("status") 
	public void status_server(@Suspended final AsyncResponse asyncResponse) {
		new Thread(new Runnable() {
            @Override
            public void run() {
            	asyncResponse.resume("200 OK");
            	return ;
            }
		}).start();
		return ;
	}
	
    @POST
    @Path("message")
    public void receive_message(@HeaderParam("Authorization") String authorization, String body, @Suspended final AsyncResponse asyncResponse){
    	 new Thread(new Runnable() {
             @Override
             public void run() {
		         // It checks the security with the "authorization" header
		     	User auth = new SecurityFilter().authenticate(authorization);
		     	if (auth == null) {
		     		asyncResponse.resume(Response.status(Status.UNAUTHORIZED).build());
		                 return;
		     	}
		     	System.out.println("\n Microservicio: " + auth.getUsername() + "\n");
		    	System.out.println("Mensaje recibido.");
		        
		    	// It obtains the user's message
		    	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		    	
		        Data message = null;
		        try {
		            message = mapper.readValue(body, Data.class); 
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        
		        // It obtains the user and it updates their profile with this microservice
		        Person user = findUserDB(message.getUser());
		        
		        if (message.getMessage().getBody().equals("") || message.getMessage().getAttachments() != null) {
		        	// It sends the message to the user
		    		String b = "";
		    		try {
		    			Message mAux = new Message();
		    			mAux.setBody(format("Vaya, parece que el mensaje me ha llegado vacío o era una foto, vuelve a intentarlo 🙂"));
		    			b = mapper.writeValueAsString(new Data(message.getUser(), mAux));
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    		}
		    		asyncResponse.resume(b);
		    		return;
		        }
		        
		        String botname="elena"; String path=System.getProperty("user.dir");
		        String file = path + "/bots/elena/aiml/interaction.aiml";
		        
		        String m = response(botname, path, file, user, message.getMessage().getBody(), body);
		        
		        System.out.println("Respuesta: " + m); 
		
		        // To be able to send more than one message in the same interaction, in the AIML file the messages need to be separated with ';'
		        String[] parts = m.split(";");
		        
		        String bAux = "";
		        for (int i = 0; i < parts.length-1; i++) {
		        // It sends the message to the user
		     		try {
		     			Message mAux = new Message();
		     			mAux.setBody(format(parts[i]));
		     			bAux = mapper.writeValueAsString(new Data(message.getUser(), mAux));
		     		} catch (IOException e) {
		     			e.printStackTrace();
		     		}
		     		
		     		try {
		    			// It checks if the token has expired or not
		    	        token.checkToken(client_id, client_secret);
		    	        new Rest().post (url_botCore + "send/message", bAux, token.getId_token());
		    		} catch (AuthenticationException e) {
		    			do {
		    			token.getToken(client_id, client_secret);
		    			} while (token.getId_token().equals(""));
		    			token.setTokenDB(client_id);
		    			new Rest().post (url_botCore + "send/message", bAux, token.getId_token());
		    		}
		     		
		     		try {
						Thread.sleep(pause);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
		        
		    	// It sends the message to the user
				String b = "";
				try {
					Message mAux = new Message();
					mAux.setBody(format(parts[parts.length-1]));
					b = mapper.writeValueAsString(new Data(message.getUser(), mAux));
				} catch (IOException e) {
					e.printStackTrace();
				}
				asyncResponse.resume(b);
				return;
    	
             }
    	    }).start();
    }
    
    /*
     * Function that gets the response message and executes the corresponding code
     */
    private String response (String botname, String path, String file, Person user, String message, String body) {
    	Bot bot = new Bot(botname, path);
        Chat chatSession = new Chat(bot); 
        AIMLProcessor.extension = new Extension();
        
        Nodemapper leaf = chatSession.bot.brain.match(message, user.getContext(), user.getType());
        ParseState ps = new ParseState(0, chatSession, message, user.getContext(), user.getType(), null);
        File initialFile = new File(file);
        
        InputStream input = null;
		try {
			input = new FileInputStream(initialFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        Document document;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            document = documentBuilder.parse(input);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse region metadata file: " + e.getMessage(), e);
        }

        NodeList regionNodes = document.getElementsByTagName("category");
        Node node = regionNodes.item(regionNodes.getLength() - leaf.category.categoryCnt + leaf.category.getCategoryNumber());
        
        String result = "";
        boolean microservice = false;
        try {
            NodeList childList = node.getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {
            	
                Node child = childList.item(j);
                if (child.getNodeName().equals("microservice")) {
                	result = AIMLProcessor.extension.recursEval(child, ps);
                	System.out.println("Result: " + result);
                	user.setRunningFunctionality(result);
                	microservice = true;
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(!microservice)
        	user.setRunningFunctionality(null);
        
        // It gets the message that needs to be sent to the user
        String m = AIMLProcessor.respond(message, user.getContext(), user.getType(), chatSession);
        String[] parts = m.split(";");
        
        // <oob> element
        int x = parts[parts.length-1].indexOf("<oob>");
        String[] newVariables = new String[3];
        if (x > -1) {
	        String xml = parts[parts.length-1].substring(x);
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = null;
	        try {
	            db = dbf.newDocumentBuilder();
	            InputSource is = new InputSource();
	            is.setCharacterStream(new StringReader(xml));
	            try {
	                Document doc = db.parse(is);
	                result = doc.getDocumentElement().getTextContent();
	                System.out.println(result);
	                
	                newVariables = run(result, user, m, message, chatSession, file);
	            } catch (SAXException e) {
	                // handle SAXException
	            } catch (IOException e) {
	                // handle IOException
	            }
	        } catch (ParserConfigurationException e1) {
	            // handle ParserConfigurationException
	        }
	        
	        // It updates the 'context' variable
	        user.setContext(parts[parts.length-1].substring(0, x));
	        
	        // It updates the message to be sent to the user, in case the 'run' function needs to change it
	        if (!newVariables[0].equals("")) {
	        	m = newVariables[0];
	        } else {
		        x = m.indexOf("<oob>");
		        m = m.substring(0, x);
	        }
	        
	        // It updates the functionality to save, in case the 'run' function needs to change it
	        if (!(newVariables[1] == null)) {
	        	if (newVariables[1].equals("SI_MICROSERVICIO"))
	        		user.setRunningFunctionality(client_id);
	        	else
	        		user.setRunningFunctionality(null);
	        }
	        
	        // It changes the 'context' variable. It does not modify the variable before executing the run function, 
	        // because the old context within the function is used
	        if (!(newVariables[2].equals("")))
	        	user.setContext(newVariables[2]);
	        
        } else {
        	user.setContext(parts[parts.length-1]);
        }
        
        modifyUserDB(user);
        
       return m;
    }
    
    /*
     * Function that executes the functions indicated in the AIML file
     */
    private String[] run (String result) {
    	
    	switch (result) {
    		case "EXAMPLE":
    			// Do what operations it needs
            	return new String[] {"", null, ""};
    				
            default:
            	return new String[] {"", null, ""};
        }
    }
    
    /*
     * Function that put the String s in URL format
     */
    private String format (String s) {
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
    
    /*
     * Function that obtains the object in JSON format and returns it as a string
     */
    private String getJson (String response) {
        String [] a = response.split("\"results\": \\[", 2);
        String [] aux = (a[1]).split("], \"id\":");
        return aux[0];
    }
    
 // ----------------------------------   DATABASE FUNCTIONS   -----------------------------------------------
    /*
     * Function that searches a user in the database
     */
    public Person findUserDB(String number){
    	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	Rest rest = new Rest();
    	
    	String url = "users/_find?criteria={\"id_signal\":\"" + number + "\"}";
        Request r = new Request(url, null);
        Person user =  new Person();
        try {
            String person = "";

            try {
    			// It checks if the token has expired or not
    	        token.checkToken(client_id, client_secret);
    	        person = rest.post(url_botCore + "db/request", mapper.writeValueAsString(r), token.getId_token());
    		} catch (AuthenticationException e) {
    			do {
    			token.getToken(client_id, client_secret);
    			} while (token.getId_token().equals(""));
    			token.setTokenDB(client_id);
    			person = rest.post(url_botCore + "db/request", mapper.writeValueAsString(r), token.getId_token());
    		}
            
            if (!person.contains("{\"ok\": 1, \"results\": []")){
                String json = getJson(person);
                System.out.println("GetJson: " + getJson(person));
                user = mapper.readValue(json, Person.class); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return user;
    }
    
    /*
     * Function that modifies a user in the database
     */
    public void modifyUserDB(Person user){
    	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	Rest rest = new Rest();
    	
        try {
        	Request r = new Request("users/_update", "criteria={\"id\":\"" + user.getId() + "\"}&newobj=" +  mapper.writeValueAsString(user));
    		
    		try {
    			// It checks if the token has expired or not
    	        token.checkToken(client_id, client_secret);
    	        rest.post(url_botCore + "db/request", mapper.writeValueAsString(r), token.getId_token());
    		} catch (AuthenticationException e) {
    			do {
    			token.getToken(client_id, client_secret);
    			} while (token.getId_token().equals(""));
    			token.setTokenDB(client_id);
    			rest.post(url_botCore + "db/request", mapper.writeValueAsString(r), token.getId_token());
    		}
    	} catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
