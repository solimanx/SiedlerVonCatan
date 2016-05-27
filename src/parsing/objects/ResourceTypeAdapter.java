package parsing.objects;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 *
 * <b>4.6: "Rohstoffe"</b>
 * <p>
 * Determines the amount of each resource that a player owns.
 * </p>
 *
 * @see parsing.objects.PlayerTypeAdapter
 * @version 0.1
 */
public class ResourceTypeAdapter extends TypeAdapter<String>{

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