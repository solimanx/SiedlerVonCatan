package ai;

import java.io.IOException;

import enums.Color;
import parsing.Parser;
import parsing.Response;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;

public class PrimitiveAIOutputHandler {
	// To get the first available color

	PrimitiveAI ai;
	private Parser parser;

	public PrimitiveAIOutputHandler(PrimitiveAI primitiveAI) {
		ai = primitiveAI;
		parser = new Parser();
	}

	protected void respondHello(String version) {
		ProtocolHello ph = new ProtocolHello(version, null);
		Response r = new Response();
		r.pHello = ph;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void respondStartGame() {
		ProtocolClientReady pcr = new ProtocolClientReady();
		Response r = new Response();
		r.pClientReady = pcr;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void respondProfile(int colorCounter) {
		ProtocolPlayerProfile ppp;
		switch (colorCounter) {
		case 0:
			ppp = new ProtocolPlayerProfile("BlueBro", Color.BLUE);
			break;
		case 1:
			ppp = new ProtocolPlayerProfile("RedBro", Color.RED);
			break;
		case 2:
			ppp = new ProtocolPlayerProfile("Orange Destroyer", Color.ORANGE);
			break;
		case 3:
			ppp = new ProtocolPlayerProfile("Walter White", Color.WHITE);
			break;
		default:
			ppp = null;
		}
		Response r = new Response();
		r.pPlayerProfile = ppp;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
