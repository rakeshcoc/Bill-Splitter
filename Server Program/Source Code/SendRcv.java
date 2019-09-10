import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;

import bsclient.expense;
public class SendRcv {
	public static float a[];
	public static void main(String[] args)throws Exception {
		InetAddress group=null;
		int port=0;
		MulticastSocket socket=null;
	        byte[] data=new byte[1000];
        
        try
        {
           	group=InetAddress.getByName("234.1.2.3");
        	port=Integer.parseInt("4565");
        	Thread theThread=new Thread(new ReadThread(group, port));
        	theThread.start();
        	Thread.sleep(20000);
        	System.out.println("No of connections:"+ReadThread.ptr);
        	a=new float[ReadThread.ptr+1];
        
        	int flag=ReadThread.ptr;
        	
        	for(int i=0;i<flag;i++)
        		a[i]=0;
        	
           	socket=new MulticastSocket(port);
           	
           	String xx="Now You can Start giving ur data\n";
           	
           	byte[] data1=new byte[100];
           	data1=xx.getBytes();
           	DatagramPacket packet1=new DatagramPacket(data1, data1.length,group,port);
           	
           	socket.send(packet1);
           	socket.joinGroup(group);
           	
           	while(flag!=0)
        	{        	       	
           		expense exps = new expense();
           	System.out.println("Back to Receive msg");
           	data=new byte[1000];
           	DatagramPacket packet=new DatagramPacket(data, data.length);
        	socket.receive(packet);
        	ByteArrayInputStream bis = new ByteArrayInputStream(data);
        	ObjectInputStream ois = new ObjectInputStream(bis);
        	exps = (expense) ois.readObject();
        	String temp=exps.purpose;
            System.out.println(temp);
            if(temp.equalsIgnoreCase("stop"))
            {
            	System.out.println(ReadThread.name[exps.paidby]+" done");
            	flag--;
            	System.out.println("No of cnnection still"+flag);
            }
            else
            {
            System.out.print(":->"+exps.exp);
            System.out.println("  add : "+ReadThread.name[exps.paidby]);
            a[exps.paidby]+=exps.exp;
            }
            }
           	System.out.println("now calculating");
        	socket.close();
        	for(int i=1;i<=ReadThread.ptr;i++)
            {
            	System.out.println(a[i]+" ");
            }
        	split.div(a);
        }catch(Exception e)
        {
        	e.getStackTrace();
        }
        
        }
	}


