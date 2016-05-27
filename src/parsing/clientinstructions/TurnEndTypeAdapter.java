package parsing.clientinstructions;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 *
 * <b>8.3: "Zug beenden"</b>
 * <p>
 * To end the turn.
 * </p>
 *
 * @version 0.1
 */
public class TurnEndTypeAdapter extends TypeAdapter<String> {

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
