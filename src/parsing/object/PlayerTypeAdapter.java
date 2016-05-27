package parsing.object;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 *
 * <b>4.5: "Spieler"</b>
 * <p>
 * Determines player's ID, colour, name, current status, victory points and resources owned.
 * </p>
 *
 * @version 0.1
 */
public class PlayerTypeAdapter extends TypeAdapter<String>{

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
