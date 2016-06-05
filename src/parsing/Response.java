package parsing;

import com.google.gson.annotations.SerializedName;

import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.clientinstructions.ProtocolHarbourRequest;
import protocol.clientinstructions.ProtocolRobberLoss;
import protocol.clientinstructions.ProtocolRobberMovementRequest;
import protocol.clientinstructions.trade.ProtocolTradeAccept;
import protocol.clientinstructions.trade.ProtocolTradeRequest;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.configuration.ProtocolVictory;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.messaging.ProtocolServerConfirmation;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolCosts;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolRobberMovement;
import protocol.serverinstructions.ProtocolStatusUpdate;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradePreview;

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

	@SerializedName("Spiel beendet")
	public ProtocolVictory pVictory; // new in 0.2

	// server instructions

	@SerializedName("Bauvorgang")
	public ProtocolBuild pBuild;

	@SerializedName("Würfelwurf")
	public ProtocolDiceRollResult pDRResult;

	@SerializedName("Ertrag")
	public ProtocolResourceObtain pRObtain;

	@SerializedName("Statusupdate")
	public ProtocolStatusUpdate pSUpdate;

	@SerializedName("Kosten")
	public ProtocolCosts pCosts; // new in 0.2

	@SerializedName("Räuber versetzt")
	public ProtocolRobberMovement pRobberMovement; // new in 0.2

	// messaging

	@SerializedName("Chatnachricht")
	public ProtocolChatReceiveMessage pChatReceive;

	@SerializedName("Chatnachricht senden")
	public ProtocolChatSendMessage pChatSend;

	@SerializedName("Serverantwort")
	public String pServerConfirmation;

	// Client Instructions

	@SerializedName("Bauen")
	public ProtocolBuildRequest pBuildRequest;

	@SerializedName("Würfeln")
	public ProtocolDiceRollRequest pDiceRollRequest;

	@SerializedName("Zug beenden")
	public ProtocolEndTurn pEndTurn;

	@SerializedName("Karte abgeben")
	public ProtocolRobberLoss pRobberLoss; // new in 0.2

	@SerializedName("Räuber versetzen")
	public ProtocolRobberMovementRequest pRobberMoveRequest; // new in 0.2

	@SerializedName("Seehandel")
	public ProtocolHarbourRequest pHarbourRequest; // new in 0.2

	// Trade Instructions

	@SerializedName("Handel anbieten")
	public ProtocolTradeRequest pTradeRequest; // new in 0.2

	@SerializedName("Handelsangebot")
	public ProtocolTradePreview pTradePreview; // new in 0.2

	@SerializedName("Handel annehmen")
	public ProtocolTradeAccept pTradeAccept; // new in 0.2

	@SerializedName("Handelsangebot angenommen")
	public ProtocolTradeConfirmation pTradeConfirm; // new in 0.2

}
