package parsing.objects;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import parsing.JSONInterface;

/**
 *
 * <b>4.4: "Karte"</b>
 * <p>
 * Determines the board: fields, buildings and harbours, as well as robber's
 * location.
 * </p>
 *
 * @version 0.1
 */
public class BoardTypeAdapter extends TypeAdapter<String> {

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
