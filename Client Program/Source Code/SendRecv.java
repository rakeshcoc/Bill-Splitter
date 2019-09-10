package bsclient;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

public class SendRecv {

    public static ObservableList data;
    public static MulticastSocket mySocket;
    public static boolean slp = true;
    
    public static void InitMulticast() {

        try {
            mySocket = new MulticastSocket(4565);

        } catch (IOException ex) {
            Logger.getLogger(SendRecv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void sendtogroup(expense ob) {
        try {
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bais);

            oos.writeObject(ob);
            oos.flush();
            byte[] buffer = bais.toByteArray();
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("234.1.2.3"), 4565);

            mySocket.send(datagram);

        } catch (IOException ex) {
            Logger.getLogger(SendRecv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static expense recievemulticast(MulticastSocket mySocket) {

        expense store = new expense();
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket pac = new DatagramPacket(buffer, 1000);
            mySocket.receive(pac);

            ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = new ObjectInputStream(bis);

            store = (expense) ois.readObject();
            
            if(!store.purpose.trim().equalsIgnoreCase("stop")){
            FXMLDocumentController j=new FXMLDocumentController();
            j.updateTable(store);
            }
            } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SendRecv.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (store);
    }

    public static void connectToServer(String user,String pass) {

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            InetAddress group = BSclient.serverip;
            int port = Integer.parseInt("4566");
            DatagramSocket socket = new DatagramSocket(port);
                      
            String messg=user+" "+pass;
            byte[] data=messg.getBytes();
            DatagramPacket datagram = new DatagramPacket(data, data.length, group, port);
           
            socket.send(datagram);
            
            
            data = new byte[1000];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            socket.setSoTimeout(5000);
            try{
            socket.receive(packet);
            }catch(SocketTimeoutException ex){
            FXMLDocumentController.DisplayWarning("Server not Responding", "Sorry but the Server is not responding properly\nPlease recheck the IP you entered");
            System.exit(0);
            }
            String msg = new String(packet.getData());
           // System.out.println(msg);
            if (msg.trim().equals("Wrong Password")) {
                FXMLDocumentController.DisplayWarning("Wrong password", "Sorry you entered wrong password");
                java.lang.System.exit(0);
            } else {
                BSclient.clientid = Integer.parseInt(msg.split(" ")[msg.split(" ").length - 1].trim());
                FXMLDocumentController.Displaymsg("Login Successful","Please wait for few seconds while other users Join the server.\n");
                InetAddress gp = InetAddress.getByName("234.1.2.3");
                Thread thehread = new Thread(new Readhread(gp, 4565));
                thehread.start();
                while (slp) {
                    Thread.sleep(500);
                }

            }
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recieveresults(){
        
        try {
            byte[] data=null;
            DatagramSocket socket=new DatagramSocket(4566);
            String results=new String();
            long startTime = System.currentTimeMillis(); 
            boolean st=true;
            //System.currentTimeMillis()-startTime)<30000
            while(st)
            {
                data=new byte[1000];
                DatagramPacket packet=new DatagramPacket(data, 1000);
                try{
                socket.receive(packet);
                results=results+"\n"+new String(packet.getData());
                
                }
                catch(SocketTimeoutException s)
                        {
                            st=false;   
                            FXMLDocumentController.Displaymsg("BILL SPLIT", results);    
                        }
                socket.setSoTimeout(1000);
            }
            socket.close();
            
        } catch (Exception ex) {
            Logger.getLogger(SendRecv.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    
}
