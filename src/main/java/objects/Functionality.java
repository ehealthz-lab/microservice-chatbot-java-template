package main.java.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sroca on 04/07/2017.
 */

public class Functionality {

    // Data associated with the functionality
    private String message; 
    private String address;
    private String client_id; // ID

    private boolean menu;
    private List<String> canUse;

    private ArrayList<String> organization;
    
    public Functionality() {
        message = "";
        address = "";
        client_id = "";

        menu = false;
        canUse = null;
        organization = new ArrayList<>();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public List<String> getOrganization() {
        return organization;
    }

    public void setOrganization(ArrayList<String> organization) {
        this.organization = organization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getClient_id () {
    	return client_id;
    }
    
    public void setClient_id (String client_id) {
    	this.client_id = client_id;
    }

    public boolean isMenu() {
        return menu;
    }

    public void setMenu(boolean menu) {
        this.menu = menu;
    }

    public List<String> getCanUse() {
        return canUse;
    }

    public void setCanUse(List<String> canUse) {
        this.canUse = canUse;
    }

}
