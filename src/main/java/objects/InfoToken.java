package main.java.objects;

public class InfoToken {
	
	private String iss;
	private int iat;
	private String sub;
	private long exp;
	private String openid;
	
    public InfoToken () {
    	iss = "";
    	iat = 0;
    	sub = "";
    	exp = 0;
    	openid = "";
    	
    }

    public InfoToken (String iss, int iat, String sub, int exp, String openid) {
    	this.iss = iss;
        this.iat = iat;
        this.sub = sub;
        this.exp = exp;
        this.openid = openid;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }
    
    public int getIat() {
        return iat;
    }

    public void setIat(int iat) {
        this.iat = iat;
    }
	
    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
    
    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }
    
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
    
}
