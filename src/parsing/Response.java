package parsing;

import com.google.gson.annotations.SerializedName;

import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.clientinstructions.ProtocolHarbourRequest;
import protocol.clientinstructions.ProtocolRobberLoss;
import protocol.clientinstructions.ProtocolRobberMovementRequest;
import protocol.clientinstructions.trade.*;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.configuration.ProtocolVictory;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolCosts;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolRobberMovement;
import protocol.serverinstructions.ProtocolStatusUpdate;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradeIsCanceled;
import protocol.serverinstructions.trade.ProtocolTradeIsCompleted;
import protocol.serverinstructions.trade.ProtocolTradeIsRequested;
import protocol3.clientinstructions.ProtocolBuyDevelopmentCards;
import protocol3.clientinstructions.ProtocolDevelopmentCards;
import protocol3.object.ProtocolInventionCard;
import protocol3.object.ProtocolMonopolyCard;
import protocol3.object.ProtocolRoadBuildingCard;
import protocol3.severinstructions.*;

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

    @SerializedName("Karten abgeben")
    public ProtocolRobberLoss pRobberLoss; // new in 0.2

    @SerializedName("Räuber versetzen")
    public ProtocolRobberMovementRequest pRobberMoveRequest; // new in 0.2

    @SerializedName("Seehandel")
    public ProtocolHarbourRequest pHarbourRequest; // new in 0.2

    // Trade Instructions

    @SerializedName("Handel anbieten")
    public ProtocolTradeRequest pTradeRequest; // new in 0.2

    @SerializedName("Handelsangebot")
    public ProtocolTradeIsRequested pTradeIsRequested;// new in 0.2

    @SerializedName("Handel annehmen")
    public ProtocolTradeAccept pTradeAccept; // new in 0.2

    @SerializedName("Handelsangebot angenommen")
    public ProtocolTradeConfirmation pTradeConfirm; // new in 0.2

    @SerializedName("Handel abschließen")
    public ProtocolTradeComplete pTradeComplete;// new in 0.2

    @SerializedName("Handel ausgeführt")
    public ProtocolTradeIsCompleted pTradeIsCompleted;// new in 0.2

    @SerializedName("Handel abbrechen")
    public ProtocolTradeCancel pTradeCancel;// new in 0.2

    @SerializedName("Handelsangebot abgebrochen")
    public ProtocolTradeIsCanceled pTradeIsCanceled;// new in 0.2

    // Serverinstructions in Protocol 0.3

    @SerializedName("Größte Rittermacht")
    public ProtocolBiggestKnightProwess pBiggestKnightProwess;// new in 0.3

    @SerializedName("Erfindung")
    public ProtocolInventionCardInfo pInventionCardInfo;// new in 0.3

    @SerializedName("Längste Handelsstraße")
    public ProtocolLongestRoad pLongestRoad;// new in 0.3

    @SerializedName("Monopol")
    public ProtocolMonopolyCardInfo pMonopolyCardInfo;// new in 0.3

    @SerializedName("Ritter ausspielen")
    public ProtocolPlayKnightCard pplayKnightCard;// new in 0.3

    @SerializedName("Straßenbaukarte ausspielen")
    public ProtocolRoadBuildingCardInfo pRoadBuildingCardInfo;// new in 0,3

    // Clientinstructions in Protocol 0.3

    @SerializedName("Entwicklungskarte kaufen")
    public ProtocolBuyDevelopmentCards pBuyDevCards; // new in 0.3

    @SerializedName("Entwicklungskarte")
    public ProtocolDevelopmentCards pDevelopmentCard; // new in 0.3

    // Object in Protocol 0.3

    @SerializedName("Erfindungskarte")
    public ProtocolInventionCard pInvention; // new in 0.3

    @SerializedName("Monopolkarte")
    public ProtocolMonopolyCard pMonopolyCard; // new in 0.3

    @SerializedName("Strassenbaukarte")
    public ProtocolRoadBuildingCard pRoadBuildCard; // new in 0.3

    @SerializedName("Entwicklungskarte gekauft")
    public ProtocolBoughtDevelopmentCard pBoughtDevelopmentCard;// new in 0.3


}
