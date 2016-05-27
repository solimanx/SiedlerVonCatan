package parsing.messaging;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import parsing.JSONInterface;

/**
 * 
 * <b>5.2: "Chat senden"</b>
 * <p>
 * Sending a message to the other players.
 * </p>
 * 
 * @version 0.1
 */
public class ChatSendMessageTypeAdapter extends TypeAdapter<String>{

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
