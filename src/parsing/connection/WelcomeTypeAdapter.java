package parsing.connection;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import parsing.JSONInterface;

/**
 *
 * <b>3: "Verbindungsaufbau"</b>
 * <p>
 * Check if server is compatible with the client via protocol and version.
 * </p>
 *
 * @version 0.1
 */
public class WelcomeTypeAdapter extends TypeAdapter {

	@Override
	public void write(JsonWriter out, Object value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object read(JsonReader in) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}



}
