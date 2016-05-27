package parsing.messaging;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 *
 * <b>5.1: "Bestätigungen und Fehler"</b>
 * <p>
 * Messages from server that confirm/deny actions of client.
 * </p>
 *
 * @version 0.1
 */
public class ServerConfirmationTypeAdapter extends TypeAdapter<String> {

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
