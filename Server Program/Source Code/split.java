import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class split extends SendRcv
{
static InetAddress ip1[]=new InetAddress[ReadThread.ptr];
static InetAddress ip2[]=new InetAddress[ReadThread.ptr];
static String na1[]=new String[ReadThread.ptr];
static String na2[]=new String[ReadThread.ptr];
public static void div(float a[])throws Exception
	{
		try
		{
	int n=ReadThread.ptr+1;
	int port=Integer.parseInt("4566");
	DatagramSocket s1=new DatagramSocket();
	int k=0,j=0;
		float neg[],pos[];
		float sum=0;
		for(int i=1;i<n;i++)
			sum=sum+a[i];
		float avg=sum/ReadThread.ptr;
		System.out.println("Average amount for all "+avg);
		if(ReadThread.ptr==1)
		{
			String m="Don't Misuse it Sir,you need to pay for Yourself ";
			byte[] data1;
			data1=m.getBytes();
			DatagramPacket p1=new DatagramPacket(data1, data1.length,ReadThread.ip[1],port);
	        s1.send(p1);	
		}
		else
		{
		for(int i=1;i<n;i++)
			a[i]=a[i]-avg;
		for(int i=1;i<n;i++)
			System.out.println(a[i]);
		for(int i=1;i<n;i++)
		{
		if(a[i]<0)
		 k++;
		else
		 j++;
		}
		int c1=1,c2=1;
		neg=new float[k+1];
		
		pos=new float[j+1];
		int num1[]=new int[k+1];
		int num2[]=new int[j+1];
		for(int i=1;i<n;i++)
		{
		if(a[i]<0)
		{
		 neg[c1]=a[i];
		 num1[c1]=i;
		 ip1[c1]=ReadThread.ip[i];
		 na1[c1++]=ReadThread.name[i];
		}
		 else
		 {
	     pos[c2]=a[i];
	     num2[c2]=i;
	     ip2[c2]=ReadThread.ip[i];
	     na2[c2++]=ReadThread.name[i];
		 }
		}
		c1++;
		c2++;
		int i=1;j=1;
		
	while((i+1)!=c1 &&(j+1)!=c2)
	{
		String str1,str2;
		byte[] data1;
		byte[] data2;
		if(neg[i]==0)
		{
			neg[i]=0;
			i++;
			continue;
		}
		if(pos[j]==0)
		{
			pos[j]=0;
			j++;
			continue;
		}
		if(Math.abs(neg[i])>pos[j])
		{
			neg[i]+=pos[j];
			
			str1="You will pay"+pos[j]+" Rs. to "+na2[j];
			str2="You will take"+pos[j]+" Rs. from "+na1[i];
			System.out.println();
			pos[j]=0;
			
			data1=str1.getBytes();
			data2=str2.getBytes();
			DatagramPacket p1=new DatagramPacket(data1, data1.length,ip1[i],port);
			DatagramPacket p2=new DatagramPacket(data2, data2.length,ip2[j],port);
		    s1.send(p1);
		    s1.send(p2);
			j++;
		}
		else if (Math.abs(neg[i])<pos[j]) {
			pos[j]+=neg[i];
			str1="You will pay"+Math.abs(neg[i])+" Rs. to "+na2[j];
			str2="You will take"+Math.abs(neg[i])+" Rs. from "+na1[i];
			System.out.println();
			neg[i]=0;
			data1=str1.getBytes();
			data2=str2.getBytes();
			DatagramPacket p1=new DatagramPacket(data1, data1.length,ip1[i],port);
			DatagramPacket p2=new DatagramPacket(data2, data2.length,ip2[j],port);
		    s1.send(p1);
		    s1.send(p2);
			i++;
		}
		else
		{
			str1="You will pay"+pos[j]+" Rs. to "+na2[j];
			str2="You will take"+pos[j]+" Rs. from "+na1[i];
			System.out.println();
			data1=str1.getBytes();
			data2=str2.getBytes();
			DatagramPacket p1=new DatagramPacket(data1, data1.length,ip1[i],port);
			DatagramPacket p2=new DatagramPacket(data2, data2.length,ip2[j],port);
		    s1.send(p1);
		    s1.send(p2);
			neg[i++]=0;
		pos[j++]=0;
		}
	}
	s1.close();
}}catch(Exception e)
		{
	e.printStackTrace();
		}
		}
}

