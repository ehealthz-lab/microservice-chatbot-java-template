package main.java.objects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sroca on 10/05/2017.
 */

public class Person {

    // User data
    private String name;
    private String surname;
    private String id; // ID
    private String id_signal; // Phone number
    
    private String type;
    private String runningFunctionality;
    private String patient;
    private String context;

    private boolean terms_accepted;
    private boolean admin;
    private boolean registered;
    private boolean discarded;
    private Date lastInteraction;

    private ArrayList<String> listFunctionalities;

    /*
    Class constructor
     */
    public Person () {
        name = "";
        surname = "";
        id = "";
        id_signal = "";

        type = "";
        runningFunctionality = null;
        patient = null;
        context = "";

        terms_accepted = false;
        admin = false;
        registered = false;
        discarded = false;
        lastInteraction = null;

        listFunctionalities = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }
    
    public String getSurname() {
        return surname;
    }

    public void setSurname(String n) {
        surname = n;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getId_signal() {
        return id_signal;
    }

    public void setId_signal(String id_signal) {
        this.id_signal = id_signal;
    }

    public String getType() {
        return type;
    }

    public void setType(String t) {
        type = t;
    }

    public String getRunningFunctionality() {
        return runningFunctionality;
    }

    public void setRunningFunctionality(String r) {
        runningFunctionality = r;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String p) {
    	patient = p;
    }
    
    public String getContext() {
        return context;
    }

    public void setContext(String c) {
        context = c;
    }
    
    public boolean getTermsAccepted() {
        return terms_accepted;
    }

    public void setTermsAccepted(boolean a) {
    	terms_accepted = a;
    }
    
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean a) {
        admin = a;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean r) {
        registered = r;
    }

    public boolean isDiscarded() {
        return discarded;
    }
    
    public void setLastInteraction(Date l) {
        lastInteraction = l;
    }

    public Date getLastInteraction() {
        return lastInteraction;
    }

    public void setDiscarded(boolean d) {
        discarded = d;
    }

    public ArrayList<String> getListFunctionalities() {
        return listFunctionalities;
    }

    public void setListFunctionalities(ArrayList<String> listFunctionalities) {
        this.listFunctionalities = listFunctionalities;
    }
    
}
