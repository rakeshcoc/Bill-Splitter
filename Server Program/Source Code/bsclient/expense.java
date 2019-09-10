package bsclient;

import java.io.Serializable;

public class expense implements Serializable{
    
	private static final long serialVersionUID = 3088980597865593669L;
	public Integer exp;
    public String purpose;
    public int paidby;
    public String message;
    public String pbname;
}

