

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StockGameSsdpClient {

    
    public static void main(String[] args) throws java.io.IOException {
        
        
        
        SSDP_Client client = new SSDP_Client();
        client.setUserAgent("StockGameClient");
        client.setSt("ssdp:Robby-Harguntas-Stock-Server");
        ArrayList<LinkedHashMap<String, String>> responses = client.send();
        
        for (LinkedHashMap<String, String> stockService : responses){
            String usn = stockService.get("USN");
            
            for (String unsplitPair : usn.split("::")){
                String[] pair = unsplitPair.split(":");
                if (pair[0].equals("name")){
                 System.out.print(pair[1]);   
                }
                
        }
            System.out.println(" @ " + stockService.get("LOCATION"));
        }
        //gets everything
        /*
         responses = client.send();
        for (LinkedHashMap<String, String> service : responses){
            System.out.println(service.get("USN"));
            System.out.println(service.get("LOCATION"));
            
            System.out.println();
            
            for (Map.Entry<String,String> header : service.entrySet()){
                System.out.println("Key = " + header.getKey() +  ", Value = " + header.getValue());
                
            }    
        }
        */
        
        
        
        }
        
        
         
            
        
        
       
       
    }
    

   
    


