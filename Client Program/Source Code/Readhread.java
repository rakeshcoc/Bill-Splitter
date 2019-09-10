package bsclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Readhread implements Runnable {

    static int MAX_LEN = 30;
    InetAddress group;
    int port;

    public Readhread(InetAddress group, int port) {
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {
        
        try {
            MulticastSocket socket = new MulticastSocket(port);
            socket.joinGroup(group);
            byte[] data=new byte[MAX_LEN];
            DatagramPacket packet=new DatagramPacket(data, data.length,group,port);
    	    socket.receive(packet);
	    SendRecv.slp=false;
            String msg=new String(packet.getData());
    	    System.out.println(msg);
            
            while (true) {
               SendRecv.recievemulticast(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
