package main.java.objects;

/**
 * Created by sroca on 19/07/2017.
 */

public class Request {

    private String url;
    private String data;

    public Request () {
        url = null;
        data = null;
    }
    
    public Request (String url, String data) {
    	this.url = url;
    	this.data = data;
    }

    public String getUrl (){
        return url;
    }

    public void setUrl (String url) {
        this.url = url;
    }

    public String getData () {
        return data;
    }

    public void setData (String data) {
        this.data = data;
    }

}
