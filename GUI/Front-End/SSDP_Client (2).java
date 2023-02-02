/* Robby Sodhi
 * J.Bains
 * 2023
 * SSDP_client is a class that is meant to be used to discover and retrieve data from the simple service discovery protocol that runs on top of the rest api server
 * it uses multicast sockets and the SSDP protocl (one step in the UPNP protocl) to announce its looking for servers and subsequently waits for and stores the responses to pass back to the caller
 * see: https://openconnectivity.org/upnp-specs/UPnP-arch-DeviceArchitecture-v2.0-20200417.pdf for an overview of the SSDp protocol
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.util.LinkedHashMap;
import java.util.ArrayList;

public class SSDP_Client {
    private static final String MULTICAST_ADDRESS = "239.255.255.250"; //this is the udp broadcast ip address, when you send a UDP packet to this address, the router broadcasts it across the network
    private static final int PORT = 1900; //SSDP standard port
    private static final int MAX_PACKET_SIZE = 65507;  //SSDP standard packet size
    private static final int DEFAULT_TIME_TO_LIVE = 255; //time to live is how far the packet will travel through the network, I don't fully understand what it changes but 255 is the maximum amount of hops you can make a broadcast perform
    private static final int DEFAULT_TIMEOUT = 5000; //timeout, we scan until this time runs out, then returns the servers. Slower networks should increase this number
    
    //standard SSDP headers
    private String userAgent; //name/identifier for the ssdp service
    private String manValue; //manufacturer information  
    private String st; //service type, for example a media server would indicate that here, in this case it is just a discovery service(find other services)

    //constructor, initative our headers
    public SSDP_Client() {
        this.userAgent = "Java SSDP Client";
        this.manValue = "ssdp:discover";
        this.st = "ssdp:all";
    }

    //allows the caller to modify the user agent(if they don't want the default ones)
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    //allows the caller to modify the manValue 
    public void setManValue(String manValue) {
        this.manValue = manValue;
    }

    //allows the caller to modify the service type
    public void setSt(String st) {
        this.st = st;
    }
    
/*
* timeout how long the method will search for upnp services before timing out IN MILISECONDS. 
* timeToLive how many routers the multicast packet can traverse before failing (I think), between 0-255 inclusive. Default: 255
* an ArrayList of all of the discovered services in the format of [{USN, LOCATION, ...}, {...}]
*/
    //method that send the UDP broadcast and returns the responses
    public ArrayList<LinkedHashMap<String, String>> send(int timeToLive, int timeout) throws IOException {
       ArrayList<LinkedHashMap<String,String>> responses = new ArrayList<LinkedHashMap<String,String>>();

        
        
        // Create the socket
        MulticastSocket socket = new MulticastSocket();
        socket.setReuseAddress(true); //standard practice for ssdp I think, not sure what this does and it doesn't seem to change anything here
        socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS)); //in the ssdp (upnp) standard, all devices are on a multicast group with that address
        
        // Set the time to live
        if (timeToLive < 0)
            timeToLive = DEFAULT_TIME_TO_LIVE;
        socket.setTimeToLive(timeToLive);
        
        
        //set the socket timeout
        if (timeout < 0)
            timeout = DEFAULT_TIMEOUT;
        socket.setSoTimeout(timeout);

        // create the message (All of the different standard ssdp headers)
        String message = "M-SEARCH * HTTP/1.1\r\n"
                + "HOST: " + MULTICAST_ADDRESS + ":" + PORT + "\r\n"  
                + "MAN: \"" + manValue + "\"\r\n"
                + "ST: " + st + "\r\n"
                + "MX:" + (timeout / 1000) + "\r\n" //mx is how long the services we are trying to discover should wait before responding, this is to ensure that both sides are ready to receive and respond to the requests (so they don't overlap and get ignored, because UDP is an unhanshaked protocol so it is not data safe)
                + "USER-AGENT: " + userAgent + "\r\n\r\n";
        
        // Create the datagram packet
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), PORT);

        // Send the packet
        socket.send(packet);

        // keep receiving data until we timeout
        try{
        while (true){
            //get the responses from the various SSDP services
            //note this is not limited to my SSDP server, ever SSDP compliant device on the network will respond
            //if you run this, you can get access to many wifi connected devices on your network (for instance, if you have a tv with the ability for a wifi connected app to act as a remote, you could get access to that server from here)
            byte[] responseBuffer = new byte[MAX_PACKET_SIZE];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);
            String response = new String(responseBuffer).trim();
            
            //all the headers are split by newline characters
            String[] headerFields = response.split("\r\n");
            
            //take the service response(String) and put it in a linked hashmap like {header: data},
            LinkedHashMap<String, String> service = new LinkedHashMap<>();
            for (String headerField : headerFields) {

                String[] parts = headerField.split(":", 2);

                if (parts.length != 2){
                    continue;
                }
                
                
                service.put(parts[0], parts[1]);
            }

              responses.add(service);
        
        }
        }catch(java.net.SocketTimeoutException e){
            System.out.println("timed out");
        }
  
    
        
        socket.close();
        return responses;
    }
    
    public ArrayList<LinkedHashMap<String, String>> send() throws IOException{
        return this.send(-1, -1);
    }
}