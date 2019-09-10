package bsclient;

import java.io.Serializable;

public class expense implements Serializable{
    public static final long serialVersionUID = 3088980597865593669L;
    Integer exp;
    String purpose;
    int paidby;
    String message;
    String pbname;
    
    public Integer getExp()
    {
        return exp;
    }
    
    public String getPurpose()
    {
        return purpose;
    }
    
    
    public String getPbname()
    {
        return pbname;
    }
    
}


