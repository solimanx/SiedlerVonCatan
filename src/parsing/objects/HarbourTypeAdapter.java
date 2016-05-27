package parsing.objects;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 *
 * <b>4.3: "Häfen"</b>
 * <p>
 * Determines harbours's ID (location) and it's type.
 * </p>
 *
 * @version 0.1
 */
public class HarbourTypeAdapter extends TypeAdapter<String>{

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
