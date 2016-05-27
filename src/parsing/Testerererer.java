package parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import client.client.InputHandler;
import protocol.connection.ProtocolHello;

public class Testerererer{
	    public static void main(String[] args){

	        String jsonstring = "{ \"Hallo\" : { \"Version\" : \"JavaFXClient 0.1 (RuhendeRebellionen)\" } }";
	        System.out.println(jsonstring);
	        InputHandler handler = new InputHandler();
	        handler.sendToParser(jsonstring);
	        /*Parser p = new Parser();
	        Object o = p.parseString(jsonstring);
	        System.out.println(o.getClass());
	        ProtocolHello ph = (ProtocolHello) o;
	        System.out.println("");
	        System.out.println(ph.protokoll);
	        System.out.println(ph.version);*/

	        
	        
	       //Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	       //gson.toJson(o,System.out);
	        

	    }
	}
