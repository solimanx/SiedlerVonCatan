package debugging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parsing.Parser;
import parsing.Response;
import protocol.connection.ProtocolHello;
import protocol.messaging.ProtocolChatSendMessage;
import settings.DefaultSettings;

public class DebugClientController extends Thread {
    private static Logger logger = LogManager.getLogger(DebugClientController.class.getName());
    private Socket socket;
    private final String SERVERHOST = "aruba.dbs.ifi.lmu.de";
    private final int PORT = 10000;
    private OutputStreamWriter writer;
    private BufferedReader reader;
    Gson gson = new GsonBuilder().create();
    Parser parser = new Parser();

    @FXML
    private TextFlow textFlow;

    @FXML
    private ToggleButton quickStartButton;

    @FXML
    private ToggleButton connectButton;

    @FXML
    private Button exportText;

    @FXML
    private Button helloButton;

    @FXML
    private TextField helloTextField;

    @FXML
    private Button chatButton;

    @FXML
    private TextField chatTextField;

    @FXML
    private Button playerButton1;

    @FXML
    private TextField nameTextField1;

    @FXML
    private ComboBox<?> colorBomo;

    @FXML
    private Button startButton;

    @FXML
    private Button rollDiceButton;

    @FXML
    private Button loseCardButton;

    @FXML
    private TextField woodTextField;

    @FXML
    private TextField clayTextField;

    @FXML
    private TextField oreTextField;

    @FXML
    private TextField sheepTextField;

    @FXML
    private TextField cornTextField;

    @FXML
    private Button moveRobberButton;

    @FXML
    private TextField robberOrt;

    @FXML
    private TextField robberZiel;

    @FXML
    private Button buildButton;

    @FXML
    private TextField buildTyp;

    @FXML
    private TextField buildOrt;

    @FXML
    private Button buyDevCard;

    @FXML
    private Button endTurn;

    @FXML
    private Button randomJSON;

    @FXML
    private TextField customJSON;

    @FXML
    private TextArea textField;

    @FXML
    void buyIt(ActionEvent event) {

    }

    @FXML
    void chatHandle(ActionEvent event) {

        ProtocolChatSendMessage pcsm;
        if (chatTextField.getText().equals("")) {
            pcsm = new ProtocolChatSendMessage("");
        } else
            pcsm = new ProtocolChatSendMessage(chatTextField.getText());

        Response r = new Response();
        r.pChatSend = pcsm;
        try {
            write(parser.createString(r));
        } catch (IOException e) {
            logger.catching(Level.DEBUG,e);
            e.printStackTrace();
        }
    }

    @FXML
    void connectToServer(ActionEvent event) {
        start();
        connectButton.setDisable(true);
    }

    @FXML
    void endTurnDoit(ActionEvent event) {

    }

    @FXML
    void exportToTXT(ActionEvent event) {

    }

    @FXML
    void handleBuildButton(ActionEvent event) {

    }

    @FXML
    void handleDIceroll(ActionEvent event) {

    }

    @FXML
    void handleMoveRobber(ActionEvent event) {

    }

    @FXML
    void handleStartButton(ActionEvent event) {

    }

    @FXML
    void helloHandle(ActionEvent event) {
        ProtocolHello ph;
        if (helloTextField.getText().equals("")) {
            ph = new ProtocolHello("DebugClient 0.3 (NiedlichePixel)");
        } else
            ph = new ProtocolHello(helloTextField.getText());

        Response r = new Response();
        r.pHello = ph;
        try {
            write(parser.createString(r));
        } catch (IOException e) {
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }
    }

    @FXML
    void loseTorobber(ActionEvent event) {

    }

    @FXML
    void playerHandle(ActionEvent event) {

    }

    @FXML
    void quickStart(ActionEvent event) {

    }

    @FXML
    void randomJSONoutput(ActionEvent event) {

    }

    @Override
    public void run() {
        try {
            socket = new Socket(SERVERHOST, PORT);
            writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            System.out.println("Client connected to server.");
            logger.debug("Client connected to server");
            runDebugClient();
        } catch (IOException e) {
            logger.catching(Level.DEBUG, e);
            System.out.println("Connection to server failed.");
            logger.warn("Connection to server failed");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                logger.catching(Level.DEBUG, ie);
                ie.printStackTrace();
            }
        }

    }

    private void runDebugClient() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            textField.appendText(DefaultSettings.getCurrentTime() + " Server: " + line + "\n");
        }
    }

    public void write(String s) throws IOException {
        if (writer == null) {
            textField.appendText("Not connected to server.\n");
        } else {
            textField.appendText(DefaultSettings.getCurrentTime() + " DebugClient: " + s + "\n");
            writer.write(s + "\n");
            writer.flush();
        }
    }

    public void stopDebugClient() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }
    }

}
