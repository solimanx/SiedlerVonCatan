package parsing.messaging;

import parsing.JSONInterface;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * 
 * <b>5.3: "Chat empfangen"</b>
 * <p>
 * Receiving a message from the other players.
 * </p>
 * 
 * @version 0.1
 */
public class ChatReceiveMessageTypeAdapter extends TypeAdapter<String> {


	@Override
	public void write(JsonWriter out, String value) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String read(JsonReader in) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
