package client.client;

import parsing.Parser;
import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.messaging.ProtocolServerConfirmation;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolStatusUpdate;
/*
 * Ich bekomm das mit den Generics nicht hin beim Parser, er matcht hier immer nur auf handle(Object o) das sollte nicht sein.
 */
public class InputHandler {
private Parser parser;	
    
   public InputHandler(){
	   this.parser = new Parser();
   }
	
	public void sendToParser(String s){
		Object object = parser.parseString(s);
		System.out.println(object.getClass());
		handle(object);
	}
	
	private void handle(ProtocolHello hello){
		System.out.println("Hello gelesen!");
	}
	private void handle(ProtocolWelcome welcome){
		
	}
	private void handle(ProtocolClientReady clientReady){
		
	}
	private void handle(ProtocolGameStarted gameStarted){
		
	}
	private void handle(ProtocolError error){
		
	}
	private void handle(ProtocolPlayerProfile playerProfile){
		
	}
	
	//
	
	private void handle(ProtocolChatReceiveMessage chatReceiveMessage){
		
	}
	private void handle(ProtocolChatSendMessage chatSendMessage){
		
	}
	private void handle(ProtocolServerConfirmation serverConfirmation){
		
	}
	
	//
	
	private void handle(ProtocolBuild build){
		
	}
	private void handle(ProtocolDiceRollResult diceRollResult){
		
	}
	private void handle(ProtocolResourceObtain resourceObtain){
		
	}
	private void handle(ProtocolStatusUpdate statusUpdate){
		
	}
	
	//
	
	
	private void handle(ProtocolBuildRequest buildRequest){
		
	}	
	private void handle(ProtocolDiceRollRequest diceRollRequest){
		
	}
	private void handle(ProtocolEndTurn endTurn){
		
	}	
	private void handle (Object o){
		System.out.println("This should not happen");
	}
	
}
