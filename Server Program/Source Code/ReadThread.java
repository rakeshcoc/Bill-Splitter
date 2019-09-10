import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
public class ReadThread implements Runnable{
	static int MAX_LEN=100;
	static int ptr=0;
	InetAddress group;
	public static InetAddress ip[]=new InetAddress[50]; 
	public static String name[]=new String[50];
    int port,i=1;
    String passwd="SECRET";
  public static boolean stop=true;
    public ReadThread(InetAddress group,int port) {
    	this.group=group;
    	this.port=port;
    }
    public void run()
    {
    	long tStart = System.currentTimeMillis();
    	try
    	{
    		DatagramSocket s1=new DatagramSocket(port+1);
    		
    		while(stop)
    		{
    			//bsclient.identity id=new bsclient.identity();
    			long tEnd = System.currentTimeMillis();
    			double elapsedSeconds = (tEnd-tStart)/ 1000.0;
    			if(elapsedSeconds>=30)
    			{
    				System.out.println("Done with all "+(ptr-1)+"members");
    				break;
    			}
    			byte[] data=new byte[MAX_LEN];
    			DatagramPacket packet=new DatagramPacket(data, data.length);
    			s1.receive(packet);
    			byte[] buf=null;
    			//ByteArrayInputStream bis = new ByteArrayInputStream(data);
            	//ObjectInputStream ois = new ObjectInputStream(bis);
      //      	try
        //    	{           	id=(bsclient.identity) ois.readObject();}
          //  	catch(Exception e){e.printStackTrace();}
    			String mes=new String(data);
    			String p=mes.split(" ")[1].trim();
    			String m;
    			if(p.equalsIgnoreCase(passwd))
    			{
    				ptr++;
    				//name[i]=id.name;
    				name[i]=mes.split(" ")[0].trim();
    				m="Congrats,You Joined as "+ptr;
    				ip[i++]=packet.getAddress();
    				
    			}
    			else
    			{
    				m="Wrong Password";
    		    }
    			buf=m.getBytes();
    			DatagramPacket p1=new DatagramPacket(buf, buf.length, packet.getAddress(),packet.getPort());
    		    s1.send(p1);
    		}
    		s1.close();
    		System.out.println("thread stopped");
    	}catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    }