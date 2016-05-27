package parsing;

import com.google.gson.annotations.SerializedName;

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

public class Response {
	   
    // connection
    @SerializedName("Hallo")
    public ProtocolHello pHello; 
    
    @SerializedName("Willkommen")
    public ProtocolWelcome pWelcome; 
    
    // configuration
    @SerializedName("Spiel starten")
    public ProtocolClientReady pClientReady;
    
    @SerializedName("Fehler")
    public ProtocolError pError;
    
    @SerializedName("Spiel gestartet")
    public ProtocolGameStarted pGameStarted;
    
    @SerializedName("Spieler")
    public ProtocolPlayerProfile pPlayerProfile;
    
   
    // server instructions
    
    @SerializedName("Bauvorgang")
    public ProtocolBuild pBuild;
    
    @SerializedName("Würfelwurf")
    public ProtocolDiceRollResult pDRResult;
    
    @SerializedName("Ertrag")
    public ProtocolResourceObtain pRObtain;
    
    @SerializedName("Statusupdate")
    public ProtocolStatusUpdate pSUpdate;
    
    
    
    // messaging
    
    @SerializedName("Chatnachricht")
    public ProtocolChatReceiveMessage pChatReceive;
    
    @SerializedName("Chatnachricht senden")
    public ProtocolChatSendMessage pChatSend;
    
    @SerializedName("Serverantwort")
    public ProtocolServerConfirmation pServerConfirmation;
    
  
    //Client Instructions

    @SerializedName("Bauvorgang")
    public ProtocolBuildRequest pBuildRequest;

    @SerializedName("Würfeln")
    public ProtocolDiceRollRequest pDiceRollRequest;
    
    @SerializedName("Zug beenden")
    public ProtocolEndTurn pEndTurn;
}
